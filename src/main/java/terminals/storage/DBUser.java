package terminals.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.models.User;
import terminals.sql.DataType;
import terminals.sql.QueryManager;
import terminals.sql.SQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBUser implements UserStorage {
//    private static final Logger LOG = LoggerFactory.getLogger(DBUser.class);
    private final static DBUser INSTANCE = new DBUser();
    private final static DBRole ROLES = DBRole.getINSTANCE();
    private final static DBDepartment DEPARTMENTS = DBDepartment.getINSTANCE();
    private final static DBTerminal TERMINALS = DBTerminal.getINSTANCE();



    private DBUser() {
    }




    public static DBUser getINSTANCE() {
        return INSTANCE;
    }


    @Override
    public List<User> findAllUsers() {
//        LOG.info("Enter method");
        List<User> resultList = new ArrayList<>();
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM users";
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(query, params, ps -> {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                resultList.add(composeUserFromDBResultSet(resultSet));
            }
        });
//        LOG.info("Exit method");
        return resultList;
    }

    @Override
    public User findUserById(int id) {
//        LOG.info("Enter method");
        User result = null;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM users WHERE user_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                User user = null;
                while (resultSet.next()) {
                    user = composeUserFromDBResultSet(resultSet);
                }
                return user;
            }).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return result;
    }

    private User composeUserFromDBResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("user_id");
        String login = resultSet.getString("user_login");
        String password = resultSet.getString("user_password");
        String name = resultSet.getString("user_name");
        String surname = resultSet.getString("user_surname");
        String role = ROLES.findRoleById(resultSet.getInt("user_role_id"));
        String department = DEPARTMENTS.findDepartmentById(resultSet.getInt("user_department_id"));
        String terminalRegId = TERMINALS.findFieldById(resultSet.getInt("terminal_id"), "terminal_reg_id", DataType.STRING);
        boolean isActive = resultSet.getBoolean("user_is_active");
        String createDate = formatDate(resultSet.getString("user_create_date"));
        String updateDate = formatDate(resultSet.getString("user_update_date"));
        User user = new User(login, password, name, surname, role, department, terminalRegId, isActive);
        user.setId(id);
        user.setCreateDate(createDate);
        user.setLastUpdateDate(updateDate);
        return user;
    }



    @Override
    public int findIdByField(String fieldName, String value) {
//        LOG.info("Enter method");
        int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String queryPattern = "SELECT user_id FROM users WHERE ";
        String query = queryPattern + fieldName + " =?";
        List<Object> params = new ArrayList<>();
        params.add(value);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                int id = -1;
                while (resultSet.next()) {
                    id = resultSet.getInt("user_id");
                }
                return id;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public int terminalIsGivenToUser(int terminalId) {
//        LOG.info("Enter method");
       int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT user_id FROM users WHERE terminal_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(terminalId);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                int id = -1;
                while (resultSet.next()) {
                    id = resultSet.getInt("user_id");
                }
                return id;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public int checkOneTerminalInOneHand(int userId) {
//        LOG.info("Enter method");
        int result = 0;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT terminal_id FROM users WHERE user_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                int id = 0;
                while (resultSet.next()) {
                    id = resultSet.getInt("terminal_id");
                }
                return id;
            }).orElse(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public String findFieldById(int id, String fieldName, DataType dataType) {
//        LOG.info("Enter method");
        String result = "";
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT " + fieldName + " FROM users WHERE user_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                String res = "";
                while (resultSet.next()) {
                    if (dataType == DataType.STRING) {
                        res = resultSet.getString(fieldName);
                    }
                    if (dataType == DataType.INT) {
                        res = String.valueOf(resultSet.getInt(fieldName));
                    }
                    if (dataType == DataType.BOOLEAN) {
                        res = String.valueOf(resultSet.getInt(fieldName));
                    }
                }
                return res;
            }).orElse("");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public String addUser(User user) {
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "INSERT INTO users "
                + "(user_login, user_password, user_name, user_surname, " +
                "user_role_id, user_is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?);";
        String login = user.getUserLogin();
        String password = user.getUserPassword();
        String name = user.getUserName();
        String surname = user.getUserSurname();
        int role_id = ROLES.findIdByRole(user.getUserRole());
        boolean isActive = user.isActive();
        List<Object> params = new ArrayList<>();
        params.add(login);
        params.add(password);
        params.add(name);
        params.add(surname);
        params.add(role_id);
        params.add(isActive);
        long result = 0;
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();

                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint failed: users.user_login")) {
                return "Логин существует";
            }
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return String.valueOf(result);
    }

    @Override
    public String addUserWithDepartment(User user) {
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "INSERT INTO users "
                + "(user_login, user_password, user_name, user_surname, " +
                "user_role_id, user_department_id, user_is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
        String login = user.getUserLogin();
        String password = user.getUserPassword();
        String name = user.getUserName();
        String surname = user.getUserSurname();
        int role_id = ROLES.findIdByRole(user.getUserRole());
        int department_id = DEPARTMENTS.findIdByDepartment(user.getUserDepartment());
        boolean isActive = user.isActive();
        List<Object> params = new ArrayList<>();
        params.add(login);
        params.add(password);
        params.add(name);
        params.add(surname);
        params.add(role_id);
        params.add(department_id);
        params.add(isActive);

        long result = 0;
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint failed: users.user_login")) {
                return "Логин существует";
            }
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return String.valueOf(result);
    }

    @Override
    public String updateUserWithDepartment(User user) {
        String result = "OK";
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE users SET user_login=?, user_password=?, user_name=?, user_surname=?," +
                " user_role_id=?, user_department_id=?, user_is_active=?, " +
                "user_update_date = datetime(CURRENT_TIMESTAMP, 'localtime')  WHERE user_id=?";
        String login = user.getUserLogin();
        String password = user.getUserPassword();
        String name = user.getUserName();
        String surname = user.getUserSurname();
        int role_id = ROLES.findIdByRole(user.getUserRole());
        int department_id = DEPARTMENTS.findIdByDepartment(user.getUserDepartment());
        boolean isActive = user.isActive();
        List<Object> params = new ArrayList<>();
        params.add(login);
        params.add(password);
        params.add(name);
        params.add(surname);
        params.add(role_id);
        params.add(department_id);
        params.add(isActive);
        params.add(user.getId());
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE constraint failed: users.user_login")) {
                return "Пользователь с таким логином уже существует";
            }
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public String updateUser(User user) {
        String result = "OK";
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE users SET user_login=?, user_password=?, user_name=?, user_surname=?," +
                " user_role_id=?, user_department_id=null, user_is_active=?, " +
                "user_update_date = datetime(CURRENT_TIMESTAMP, 'localtime')  WHERE user_id=?";
        String login = user.getUserLogin();
        String password = user.getUserPassword();
        String name = user.getUserName();
        String surname = user.getUserSurname();
        int role_id = ROLES.findIdByRole(user.getUserRole());
        boolean isActive = user.isActive();
        List<Object> params = new ArrayList<>();
        params.add(login);
        params.add(password);
        params.add(name);
        params.add(surname);
        params.add(role_id);
        params.add(isActive);
        params.add(user.getId());
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE constraint failed: users.user_login")) {
                return "Пользователь с таким логином уже существует";
            }
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public String deleteUser(int id) {
        String result = "OK";
        String query = "DELETE FROM users WHERE user_id=?";
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        List<Object> params = new ArrayList<>();
        params.add(id);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                return "OK";
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                result = "Невозможно удалить, пока существуют зависимые записи";
            }
        }
        return result;
    }

    private String formatDate(String date) {
//        LOG.info("Enter method");
        String result = "";
        if (date != null) {
            result = date.replaceAll("-", ".");
        }
//        LOG.info("Exit method");
        return result;
    }

    @Override
    public void addTerminalToUser(int userId, int terminalId) {
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE users SET terminal_id=? WHERE user_id=?";
        List<Object> params = new ArrayList<>();
        params.add(terminalId);
        params.add(userId);
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
    }

    @Override
    public void removeTerminalFromUser(int userId) {
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE users SET terminal_id= NULL WHERE user_id=?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
    }

    /**
     * Counts terminals amount
     *
     * @param whatToCount null - all users, "inactive" - only inactive users,
     *                    "took" - only those who took the terminal.
     * @return
     */
    @Override
    public int countOfUsers(String whatToCount) {
//        LOG.info("Enter method");
        int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT COUNT(user_id) FROM users";
        if (whatToCount != null && whatToCount.equals("inactive")) {
            query = "SELECT COUNT(user_id) FROM users WHERE user_is_active = 0";
        }
        if (whatToCount != null && whatToCount.equals("took")) {
            query = "SELECT COUNT(user_id) FROM users WHERE terminal_id IS NOT NULL";
        }
        List<Object> params = new ArrayList<>();
        try {
            result = queryManager.runQuery(query, params, ps -> {
                int count = -1;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("COUNT(user_id)");
                }
                return count;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOG.info("Exit method");
        return result;
    }

}
