package com.github.moboxs.web.mvc;

import com.github.moboxs.web.mvc.controller.Controller;
import com.github.moboxs.web.mvc.controller.PageController;
import com.github.moboxs.web.mvc.controller.RestController;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

public class FrontControllerServlet extends HttpServlet {

    /**
     * 请求路径和Controller映射关系
     */
    private Map<String, Controller> controllerMapping = new HashMap<>();

    /**
     * 请求路径和映射关系缓存
     */
    private Map<String, HandlerMethodInfo> handlerMethodInfoMapping = new HashMap<>();

    /**
     * 初始化Servlet
     */
    @Override
    public void init(ServletConfig servletConfig) {
        initHandleMethods();
    }

    /**
     * 读取所有的RestController的注解元信息@Path
     */
    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String classPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            //处理方法支持的HTTP方法集合
            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Parameter[] parameters = method.getParameters();
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null) {
                    String requestPath = classPath + pathFromMethod.value();
                    handlerMethodInfoMapping.put(requestPath, new HandlerMethodInfo(requestPath, method, supportedHttpMethods, parameters));
                    controllerMapping.put(requestPath, controller);
                }
            }
        }

        System.out.println("init done");
    }

    /**
     * 获取处理方法中标注的HTTP方法
     * @param method
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }
        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }
        return supportedHttpMethods;
    }

    /**
     *
     * @param request
     * @param response
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = servletContextPath;
        //映射子路径
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(prefixPath, "//", "/"));
        // 映射到Controller
        Controller controller = controllerMapping.get(requestMappingPath);
        if (controller != null) {
            HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMapping.get(requestMappingPath);
            try {
                if (handlerMethodInfo != null) {

                    String httpMethod = request.getMethod();

                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }

                    if (controller instanceof PageController) {
                        Object ret = null;
                        if (handlerMethodInfo.getHandleMethod().getParameterCount() > 0) {
                            ret = handlerMethodInfo.getHandleMethod().invoke(controller, request, response);
                        } else {
                            ret = handlerMethodInfo.getHandleMethod().invoke(controller);
                        }

                        if (ret instanceof String) {
                            String viewPath = (String) ret;
                            ServletContext servletContext = request.getServletContext();
                            if (!viewPath.startsWith("/")) {
                                viewPath = "/" + viewPath;
                            }
                            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                            requestDispatcher.forward(request, response);
                        }
//                        PageController pageController = PageController.class.cast(controller);
//                        String viewPath = pageController.execute(request, response);
//
//                        ServletContext servletContext = request.getServletContext();
//                        if (!viewPath.startsWith("/")) {
//                            viewPath = "/" + viewPath;
//                        }
//                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
//                        requestDispatcher.forward(request, response);
                        return;
                    } else if (controller instanceof RestController) {
                        //@// TODO: 2021/3/9
                    }
                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }

        }
    }

}
