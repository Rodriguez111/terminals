package terminals.controller.logic;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PoolRollBack extends BasicDataSource {
    @Override
    public Connection getConnection() throws SQLException {
        return ConnectionRollBack.create(super.getConnection());
    }
}
