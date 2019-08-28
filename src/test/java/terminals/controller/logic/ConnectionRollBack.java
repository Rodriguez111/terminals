package terminals.controller.logic;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionRollBack {
    public static Connection create(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        return (Connection) Proxy.newProxyInstance(ConnectionRollBack.class.getClassLoader(),
                new Class[] {
                        Connection.class
                }, (proxy, method, args) -> {
            Object result = null;
            if ("close".equals(method.getName())) {
                connection.rollback();
                connection.close();
            } else {
                result = method.invoke(connection, args);
            }
            return result;
                });
    }
}
