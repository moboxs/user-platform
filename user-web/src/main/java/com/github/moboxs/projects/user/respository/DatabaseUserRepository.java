package com.github.moboxs.projects.user.respository;

import com.github.moboxs.projects.user.domain.User;
import com.github.moboxs.projects.user.sql.DBConnectionManager;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

public class DatabaseUserRepository  implements UserRepository{

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    private final static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    private final DBConnectionManager dbConnectionManager;

    public DatabaseUserRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    @Override
    public boolean save(User user) throws Exception {
        String insert = "insert into users (name, email, password, phoneNumber) values (?,?,?,?)";
        return executeQuery(insert, COMMON_EXCEPTION_HANDLER, user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber());
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
