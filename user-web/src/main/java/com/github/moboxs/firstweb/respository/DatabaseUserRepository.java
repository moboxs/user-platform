package com.github.moboxs.firstweb.respository;

import com.github.moboxs.firstweb.dao.DBConnectionManager;
import com.github.moboxs.firstweb.entity.User;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

public class DatabaseUserRepository  implements UserRepository{

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    private final DBConnectionManager dbConnectionManager;

    public DatabaseUserRepository(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    @Override
    public boolean save(User user) throws Exception {
        String insert = "insert into users ()";
        Connection connection = getConnection();
        BeanInfo beaninfo = Introspector.getBeanInfo(User.class, Object.class);
        PropertyDescriptor[] propertyDescriptors = beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.execute();



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Collection<User> getAll() {

        return null;
    }

    protected <T> T executeQuery(String sql, Object... args) {
        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i=0; i< args.length; i++) {
                Object arg = args[i];
                Class argType = arg.getClass();
                Class wrapperType = wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = PreparedStatement.class.getMethod(methodName, wrapperType);
                method.invoke(preparedStatement, i + 1, args);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return (T) resultSet;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    static Map<Class, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        preparedStatementMethodMappings.put(Long.class, "setLong");
        preparedStatementMethodMappings.put(String.class, "setString");
    }

}
