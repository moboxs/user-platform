package com.github.moboxs.projects.user.web.listener;

import com.github.moboxs.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
//        servletContextDataSource dataSource = componentContext.getComponent("jdbc/UserPlatformDB");
//        try {
//            Connection connection = dataSource.getConnection();
//
//            PreparedStatement preparedStatement = connection.prepareStatement("show databases ");
//            ResultSet execute = preparedStatement.executeQuery();
//            while (execute.next()) {
//                System.out.println(execute.getString(1));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComponentContext componentContext = ComponentContext.getInstance();
        componentContext.destroy();
    }
}
