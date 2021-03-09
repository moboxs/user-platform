package com.github.moboxs.projects.user.web.listener;

import com.github.moboxs.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ComponentContext 初始化器
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext =sce.getServletContext();
        ComponentContext componentContext = new ComponentContext();
        componentContext.init(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComponentContext componentContext = ComponentContext.getInstance();
        componentContext.destroy();
    }
}
