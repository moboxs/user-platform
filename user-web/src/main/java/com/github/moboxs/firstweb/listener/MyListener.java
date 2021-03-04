package com.github.moboxs.firstweb.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * ServletContextListener在Spring中的应用，
 * 在web.xml中注册Spring IOC容器，<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
 *
 * ContextLoaderListener extends ContextLoader implements ServletContextListener 当ServletContext创建时可以创建applicationContext对象，
 * 当ServletContext销毁时销毁applicationContext对象。
 *
 */
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContextListener.contextInitialized 被调用");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContextListener.contextDestroyed 被调用");
    }
}
