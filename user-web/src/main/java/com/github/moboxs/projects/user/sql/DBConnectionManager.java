package com.github.moboxs.projects.user.sql;


import com.github.moboxs.projects.user.domain.User;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionManager {

    private final Logger logger = Logger.getLogger(DBConnectionManager.class.getName());

    @Resource(name = "jdbc/UserPlatformDB")
    private DataSource dataSource;

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

//
//    public Connection getConnection() {
//        ComponentContext context = ComponentContext.getInstance();
//        // 依赖查找
//        DataSource dataSource = context.getComponent("jdbc/UserPlatformDB");
//        Connection connection = null;
//        try {
//            connection = dataSource.getConnection();
//        } catch (SQLException e) {
//            logger.log(Level.SEVERE, e.getMessage());
//        }
//        if (connection != null) {
//            logger.log(Level.INFO, "获取 JNDI 数据库连接成功！");
//        }
//        return connection;
//    }

    public EntityManager getEntityManager() {
        logger.info("当前 EntityManager 实现类：" + entityManager.getClass().getName());
        return entityManager;
    }

    public Connection getConnection() {
        // 依赖查找
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        if (connection != null) {
            logger.log(Level.INFO, "获取 JNDI 数据库连接成功！");
        }
        return connection;
    }

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

//    public Connection getConnection() {
//        return this.connection;
//    }


    public void releaseConnection() {
//        if (this.connection != null) {
//            try {
//                this.connection.close();
//            } catch (SQLException e) {
//                throw new RuntimeException(e.getCause());
//            }
//        }
    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";

    public static void main(String[] args) throws Exception{
        String databaseUrl = "jdbc:derby:D:\\github\\tomcat_demo\\db\\derby;create=true";
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        //Driver driver = DriverManager.getDriver("");
        Connection connection = DriverManager.getConnection(databaseUrl);
        Statement statement = connection.createStatement();
        //System.out.println(statement.execute(DROP_USERS_TABLE_DDL_SQL));
        //System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL));

        //System.out.println(statement.executeUpdate(INSERT_USER_DML_SQL));

        ResultSet resultSet = statement.executeQuery("select id,name,password,email,phoneNumber from users");

        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);

        PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();


        while (resultSet.next()) {
            User user = new User();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String tableName = resultSetMetaData.getTableName(1);
            System.out.println("当前表：" + tableName);
            System.out.println("当前表列数：" + resultSetMetaData.getColumnCount());
            for (int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
                System.out.println("当前列：" + resultSetMetaData.getColumnLabel(i));
            }

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                System.out.println(propertyDescriptor.getName() + ", " + propertyDescriptor.getPropertyType());

                String fieldName = propertyDescriptor.getName();
                Class fieldType = propertyDescriptor.getPropertyType();
                String methodName = typeMethodMappings.get(fieldType);
                 String columnLabel = fieldName;
                Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);

                // 获取User类的Setter方法
                Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                setterMethodFromUser.invoke(user, resultValue);

            }
//            user.setId(resultSet.getLong("id"));
//            user.setName(resultSet.getString("name"));
//            user.setPassword(resultSet.getString("password"));
//            user.setEmail(resultSet.getString("email"));
//            user.setPhoneNumber(resultSet.getString("phoneNumber"));
            System.out.println(user);
        }


        connection.close();
    }

    static Map<Class, String> typeMethodMappings = new HashMap<>();
    static {
        typeMethodMappings.put(Long.class, "getLong");
        typeMethodMappings.put(String.class, "getString");
    }
}
