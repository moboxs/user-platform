package com.github.moboxs.context;

import com.github.moboxs.function.ThrowableAction;
import com.github.moboxs.function.ThrowableFunction;
import sun.plugin.viewer.LifeCycleManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ComponentContext {

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    private static final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private static ServletContext servletContext;

    private Context envContext;

    private ClassLoader classloader;

    private Map<String, Object> componentMap = new LinkedHashMap<>();

    public static ComponentContext getInstance() {
        return (ComponentContext)servletContext.getAttribute(CONTEXT_NAME);
    }

    public <C> C getComponent(String name) {
        return (C) componentMap.get(name);
    }

    public void init(ServletContext servletContext) throws RuntimeException {
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);

        this.classloader = servletContext.getClassLoader();

        //初始化上下文环境
        initEnvContext();

        //实例化组件
        instantiateComponents();

        //初始化组件
        initializeComponents();
    }


    private void initEnvContext()throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    /**
     * 实例化组件
     */
    private void instantiateComponents() {
        //遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        //通过依赖查找，实例化对象（tomcat BeanFactory setter方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentMap.put(name, lookupComponent(name)));
    }

    //依赖查找
    private <C> C lookupComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name));
    }

    /**
     * 获取所有组件名
     * @return
     */
    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    private List<String> listComponentNames(String name) {

        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            // 目录 - context
            // 节点
            if (e == null) {
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classloader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    // 如果当前名称是目录的话，递归查找
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    //否则，当前名称绑定目标类型的话，添加改名称到集合中
                    String fullName = name.startsWith("/") ? element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }

            return fullNames;
        });
    }

    /**
     * 初始化组件（支持Java标准的Commons Annotation生命周期）
     */
    private void initializeComponents() {
        componentMap.values().forEach( component -> {
            Class<?> componentClass = component.getClass();
            //注入阶段 - {@link Resource}
            injectComponents(component, componentClass);
            // 初始化阶段 - {@Link PostConstruct}
            processPostConstruct(component, componentClass);
        });
    }

    //注入
    private void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields()).filter(field -> {
           int mods = field.getModifiers();
           return !Modifier.isStatic(mods) && field.isAnnotationPresent(Resource.class);
        }).forEach(field -> {
            Resource resource = field.getAnnotation(Resource.class);
            String resourceName = resource.name();
            Object injectedObject = lookupComponent(resourceName);
            field.setAccessible(true);
            try {
                //注入目标对象
                field.set(component, injectedObject);
            } catch (IllegalAccessException e) {

            }
        });
    }

    //初始
    private void processPostConstruct(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()) //非static
                        && method.getParameterCount() == 0                  // 没有参数
                        && method.isAnnotationPresent(PostConstruct.class)  // 标注@PostConstruct
                ).forEach(method -> {
            //执行目标方法
            try {
                method.invoke(component);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * 在Context中执行，通过指定ThrowableFunction 返回计算结果
     * @param function ThrowableFunction
     * @param <R></R> 返回结果类型
     * @return a
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    private <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoreException) {

        return executeInContext(this.envContext, function, ignoreException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function, boolean ignoreException) {

        R result = null;
        try {
          result = ThrowableFunction.execute(context, function);

        } catch (Throwable e) {
            if (ignoreException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public void destroy() throws RuntimeException{
        componentMap.values().forEach( component -> {
            Class<?> componentClass = component.getClass();
            //销毁阶段
            processPreDestroy(component, componentClass);
        });
        close(this.envContext);
    }

    private void processPreDestroy(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods()).filter(method ->
                !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                        method.getParameterCount() == 0 &&        // 没有参数
                        method.isAnnotationPresent(PreDestroy.class) // 标注 @PreDestroy
        ).forEach(method -> {
            // 执行目标方法
            try {
                method.invoke(component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }

}
