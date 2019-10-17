package terminals.sql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.storage.DBTerminal;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLManager {
    private static final Logger LOG = LoggerFactory.getLogger(SQLManager.class);
    private static BasicDataSource SOURCE = new BasicDataSource();

    private final static SQLManager INSTANCE = new SQLManager();

    private SQLManager() {
        initConnection();
        prepareDataStructure();
    }

    public static SQLManager getINSTANCE() {
        return INSTANCE;
    }

    public static void setSOURCE(BasicDataSource SOURCE) {
        SQLManager.SOURCE = SOURCE;
        getINSTANCE().initConnection();
        getINSTANCE().prepareDataStructure();
    }

    private void initConnection() {
        LOG.info("Enter method");
        SOURCE.setUrl("jdbc:sqlite:" + createDBDirectory()); //sqlite
        SOURCE.setDriverClassName("org.sqlite.JDBC");//sqlite
        SOURCE.setMinIdle(5);
        SOURCE.setMaxIdle(10);
        SOURCE.setMaxOpenPreparedStatements(100);
        LOG.info("Exit method");
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = SOURCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void prepareDataStructure() {
        if (!checkTable()) {
            createRolesTable();
            createDepartmentsTable();
            createUsersTable();
            createTerminalsTable();
            createRegistrationsTable();
            createDefaultRoles();
            createDefaultUser();
        }
    }

    private boolean checkTable() {
        LOG.info("Enter method");
        String query = "SELECT * FROM sqlite_master WHERE name='users'";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
boolean res = false;
        try {
            res = queryManager.runQuery(query, params, ps -> {
                boolean result = false;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    result = true;
                }
                return result;
            }).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
        return res;
    }



    private void createRolesTable() {
        String createTable = "CREATE TABLE roles "
                + "(role_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "role  character varying(25) UNIQUE NOT NULL)";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(createTable, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private void createDepartmentsTable() {
        String createTable = "CREATE TABLE departments "
                + "(department_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "department_name  character varying(30) UNIQUE NOT NULL)";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(createTable, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS users "
                + "(user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_login  character varying(20) UNIQUE NOT NULL, "
                + "user_password character varying(20),"
                + "user_name character varying(25) NOT NULL,"
                + "user_surname character varying(25) NOT NULL,"
                + "user_role_id INTEGER NOT NULL, "
                + "user_department_id INTEGER, "
                + "terminal_id INTEGER, "
                + "user_is_active BOOLEAN NOT NULL,"
                + "user_create_date DATETIME DEFAULT (DATETIME('now', 'localtime')) NOT NULL,"
                + "user_update_date DATETIME,"
                + "FOREIGN KEY(user_role_id) REFERENCES roles (role_id) ON DELETE RESTRICT,"
                + "FOREIGN KEY(user_department_id) REFERENCES departments (department_id) ON DELETE RESTRICT,"
                + "FOREIGN KEY(terminal_id) REFERENCES terminals (terminal_id) ON DELETE RESTRICT)";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(createTable, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private void createTerminalsTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS terminals "
                + "(terminal_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "terminal_reg_id character varying(10) UNIQUE NOT NULL, "
                + "terminal_model character varying(20) NOT NULL, "
                + "terminal_serial_id character varying(30) UNIQUE NOT NULL,"
                + "terminal_inventory_id character varying(20) UNIQUE NOT NULL,"
                + "terminal_comment character varying(500),"
                + "terminal_is_active BOOLEAN NOT NULL,"
                + "terminal_department_id INTEGER,"
                + "user_id  INTEGER,"
                + "terminal_create_date DATETIME DEFAULT (DATETIME('now', 'localtime')) NOT NULL,"
                + "terminal_update_date DATETIME,"
                + "FOREIGN KEY(terminal_department_id) REFERENCES departments (department_id) ON DELETE RESTRICT,"
                + "FOREIGN KEY(user_id) REFERENCES users (user_id) ON DELETE RESTRICT)";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(createTable, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private void createRegistrationsTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS registrations "
                + "(record_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "terminal_id INTEGER NOT NULL,"
                + "admin_gave_id INTEGER NOT NULL,"
                + "admin_received_id INTEGER,"
                + "record_start_date DATETIME DEFAULT (DATETIME('now', 'localtime')) NOT NULL,"
                + "record_finish_date DATETIME, "
                + "FOREIGN KEY (admin_gave_id) REFERENCES users (user_id) ON DELETE RESTRICT,"
                + "FOREIGN KEY (admin_received_id) REFERENCES users (user_id) ON DELETE RESTRICT,"
                + "FOREIGN KEY(user_id) REFERENCES users (user_id) ON DELETE RESTRICT, "
                + "FOREIGN KEY(terminal_id) REFERENCES terminals (terminal_id) ON DELETE RESTRICT"
                + ")";

        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(createTable, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private void createDefaultRoles() {
        String createTable = "INSERT INTO roles "
                + "(role) "
                + "VALUES ('root'), ('administrator'), ('user')";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(createTable, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private void createDefaultUser() {
        String insertUser = "INSERT INTO users "
                + "(user_login, user_password, user_name, user_surname, user_role_id, " +
                "user_is_active, user_create_date) "
                + "VALUES ('root', 'root', 'rootName', 'rootSurname', " +
                "(SELECT role_id FROM roles WHERE role = 'root'), "
                + "true, datetime(CURRENT_TIMESTAMP, 'localtime'))";
        QueryManager queryManager = new QueryManager(getConnection());
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(insertUser, params, (Consumer<PreparedStatement>) PreparedStatement::executeUpdate);
    }

    private static String createDBDirectory() {
        LOG.info("Enter method");
        new File("c:/terminals_resources/db").mkdirs();
        LOG.info("Exit method");
        return "c:/terminals_resources/db/terminals.db";
    }

}
