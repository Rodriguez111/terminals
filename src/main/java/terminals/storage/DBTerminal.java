package terminals.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.models.Terminal;
import terminals.models.User;
import terminals.sql.DataType;
import terminals.sql.QueryManager;
import terminals.sql.SQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBTerminal implements TerminalStorage {
    private static final Logger LOG = LoggerFactory.getLogger(DBTerminal.class);
    private final static DBTerminal INSTANCE = new DBTerminal();
    private final static DBDepartment DEPARTMENTS = DBDepartment.getINSTANCE();
    private final static DBUser USERS = DBUser.getINSTANCE();

    private DBTerminal() {
    }




    public static DBTerminal getINSTANCE() {
        return INSTANCE;
    }


    @Override
    public List<Terminal> findAllTerminals() {
        LOG.info("Enter method");
        List<Terminal> resultList = new ArrayList<>();
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM terminals";
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(query, params, ps -> {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                resultList.add(composeTerminalFromDBResultSet(resultSet));
            }
        });
        LOG.info("Exit method");
        return resultList;
    }

    @Override
    public Terminal findTerminalById(int id) {
        LOG.info("Enter method");
        Terminal result = null;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM terminals WHERE terminal_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                Terminal terminal = null;
                while (resultSet.next()) {
                    terminal = composeTerminalFromDBResultSet(resultSet);
                }
                return terminal;
            }).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
        return result;
    }

    private Terminal composeTerminalFromDBResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("terminal_id");
        String regId = resultSet.getString("terminal_reg_id");
        String terminalModel = resultSet.getString("terminal_model");
        String serialId = resultSet.getString("terminal_serial_id");
        String invId = resultSet.getString("terminal_inventory_id");
        String terminalComment = resultSet.getString("terminal_comment");
        String department = DEPARTMENTS.findDepartmentById(resultSet.getInt("terminal_department_id"));
        String userLogin = USERS.findFieldById(resultSet.getInt("user_id"), "user_login", DataType.STRING);
        boolean isActive = resultSet.getBoolean("terminal_is_active");
        String createDate = formatDate(resultSet.getString("terminal_create_date"));
        String updateDate = formatDate(resultSet.getString("terminal_update_date"));
        Terminal terminal = new Terminal(regId, terminalModel, serialId, invId, terminalComment, isActive, department, userLogin);
        terminal.setId(id);
        terminal.setCreateDate(createDate);
        terminal.setLastUpdateDate(updateDate);
        return terminal;
    }

    @Override
    public int findIdByRegId(String regId) {
        LOG.info("Enter method");
        int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT terminal_id FROM terminals WHERE terminal_reg_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(regId);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ResultSet resultSet = ps.executeQuery();
                int id = -1;
                while (resultSet.next()) {
                    id = resultSet.getInt("terminal_id");
                }
                return id;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
        return result;
    }

    @Override
    public String findFieldById(int id, String fieldName, DataType dataType) {
        LOG.info("Enter method");
        String result = "";
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT " + fieldName + " FROM terminals WHERE terminal_id = ?";
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
        LOG.info("Exit method");
        return result;
    }

    @Override
    public String addTerminal(Terminal terminal) {
        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "INSERT INTO terminals "
                + "(terminal_reg_id, terminal_model, terminal_serial_id, terminal_inventory_id, terminal_comment, terminal_is_active)"
                + "VALUES (?, ?, ?, ?, ?, ?);";
        String regId = terminal.getRegId();
        String terminalModel = terminal.getTerminalModel();
        String serialId = terminal.getSerialId();
        String invId = terminal.getInventoryId();
        String comment = terminal.getTerminalComment();
        boolean isActive = terminal.isTerminalIsActive();
        List<Object> params = new ArrayList<>();
        params.add(regId);
        params.add(terminalModel);
        params.add(serialId);
        params.add(invId);
        params.add(comment);
        params.add(isActive);
        String result = "OK";
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();

                ResultSet resultSet = ps.getGeneratedKeys();
                resultSet.getLong(1);
                return "OK";

            }).orElse("Error");
        } catch (Exception e) {
            e.printStackTrace();
            return composeErrorMessagesFromException(e);
        }
        LOG.info("Exit method");
        return result;
    }

    @Override
    public String addTerminalWithDepartment(Terminal terminal) {
        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "INSERT INTO terminals "
                + "(terminal_reg_id, terminal_model, terminal_serial_id, terminal_inventory_id, terminal_comment, terminal_is_active, " +
                "terminal_department_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
        String regId = terminal.getRegId();
        String terminalModel = terminal.getTerminalModel();
        String serialId = terminal.getSerialId();
        String invId = terminal.getInventoryId();
        String comment = terminal.getTerminalComment();
        boolean isActive = terminal.isTerminalIsActive();
        int departmentId = DEPARTMENTS.findIdByDepartment(terminal.getDepartmentName());
        List<Object> params = new ArrayList<>();
        params.add(regId);
        params.add(terminalModel);
        params.add(serialId);
        params.add(invId);
        params.add(comment);
        params.add(isActive);
        params.add(departmentId);
        String result;
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return "OK";
            }).orElse("Error");
        } catch (Exception e) {
            e.printStackTrace();
            return composeErrorMessagesFromException(e);
        }
        LOG.info("Exit method");
        return result;
    }

    @Override
    public String updateTerminal(Terminal terminal) {
        String result = "OK";
        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE terminals SET terminal_reg_id=?, terminal_model=?, terminal_serial_id=?, terminal_inventory_id=?," +
                " terminal_comment=?, terminal_is_active=?, terminal_department_id=null, " +
                "terminal_update_date = datetime(CURRENT_TIMESTAMP, 'localtime')  WHERE terminal_id=?";
        String regId = terminal.getRegId();
        String terminalModel = terminal.getTerminalModel();
        String serialId = terminal.getSerialId();
        String invId = terminal.getInventoryId();
        String comment = terminal.getTerminalComment();
        boolean isActive = terminal.isTerminalIsActive();
        int id = terminal.getId();
        List<Object> params = new ArrayList<>();
        params.add(regId);
        params.add(terminalModel);
        params.add(serialId);
        params.add(invId);
        params.add(comment);
        params.add(isActive);
        params.add(id);
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            e.printStackTrace();
            return composeErrorMessagesFromException(e);
        }
        LOG.info("Exit method");
        return result;
    }

    private String composeErrorMessagesFromException(Exception e) {
        String result = "";
        if (e.getMessage().contains("UNIQUE constraint failed: terminals.terminal_reg_id")) {
            result = "Терминал с таким учетным номером уже существует";
        }
        if (e.getMessage().contains("UNIQUE constraint failed: terminals.terminal_serial_id")) {
            result = "Терминал с таким серийным номером уже существует";
        }
        if (e.getMessage().contains("UNIQUE constraint failed: terminals.terminal_inventory_id")) {
            result = "Терминал с таким инвентарным номером уже существует";
        }
        return result;
    }

    @Override
    public String updateTerminalWithDepartment(Terminal terminal) {
        String result = "OK";
        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE terminals SET terminal_reg_id=?, terminal_model=?, terminal_serial_id=?, terminal_inventory_id=?, " +
                "terminal_comment=?, terminal_is_active=?, terminal_department_id=?, " +
                "terminal_update_date = datetime(CURRENT_TIMESTAMP, 'localtime')  WHERE terminal_id=?";
        String regId = terminal.getRegId();
        String terminalModel = terminal.getTerminalModel();
        String serialId = terminal.getSerialId();
        String invId = terminal.getInventoryId();
        String comment = terminal.getTerminalComment();
        boolean isActive = terminal.isTerminalIsActive();
        int departmentId = DEPARTMENTS.findIdByDepartment(terminal.getDepartmentName());
        int id = terminal.getId();
        List<Object> params = new ArrayList<>();
        params.add(regId);
        params.add(terminalModel);
        params.add(serialId);
        params.add(invId);
        params.add(comment);
        params.add(isActive);
        params.add(departmentId);
        params.add(id);
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            e.printStackTrace();
            return composeErrorMessagesFromException(e);
        }
        LOG.info("Exit method");
        return result;
    }

    @Override
    public String deleteTerminal(int id) {
        String result = "OK";
        String query = "DELETE FROM terminals WHERE terminal_id=?";
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        List<Object> params = new ArrayList<>();
        params.add(id);
        try {
            result = queryManager.runQuery(query, params, ps -> {
                int row = ps.executeUpdate();
                return "OK";
            }).get();
        } catch (Exception e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                result = "Невозможно удалить, пока существуют зависимые записи";
            }
            e.printStackTrace();
        }
        return result;
    }

    private String formatDate(String date) {
        LOG.info("Enter method");
        String result = "";
        if (date != null) {
            result = date.replaceAll("-", ".");
        }
        LOG.info("Exit method");
        return result;
    }

    @Override
    public int findIdByField(Map<String, String> parameters) {
        LOG.info("Enter method");
        int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = queryManager.queryComposerForExactSearch("SELECT terminal_id FROM terminals WHERE ", parameters);
        List<Object> params = new ArrayList<>();
        try {
            result = queryManager.runQuery(query, params, ps -> {
                int id = -1;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt("terminal_id");
                }
                return id;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
        return result;
    }

    /**
     * Counts terminals amount
     *
     * @param whatToCount null - all terminals, "inactive" - only inactive terminals,
     *                    "given" - only given terminals.
     * @return
     */

    @Override
    public int countOfTerminals(String whatToCount) {
        LOG.info("Enter method");
        int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT COUNT(terminal_id) FROM terminals";
        if (whatToCount != null && whatToCount.equals("inactive")) {
            query = "SELECT COUNT(terminal_id) FROM terminals WHERE terminal_is_active = 0";
        }
        if (whatToCount != null && whatToCount.equals("given")) {
            query = "SELECT COUNT(terminal_id) FROM terminals WHERE user_id IS NOT NULL";
        }
        List<Object> params = new ArrayList<>();
        try {
            result = queryManager.runQuery(query, params, ps -> {
                int count = -1;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("COUNT(terminal_id)");
                }
                return count;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
        return result;
    }

    @Override
    public void addUserToTerminal(int terminalId, int userId) {
        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE terminals SET user_id=? WHERE terminal_id=?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(terminalId);

        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
    }

    @Override
    public void removeUserFromTerminal(int terminalId) {
        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE terminals SET user_id= NULL WHERE terminal_id=?";
        List<Object> params = new ArrayList<>();
        params.add(terminalId);
        try {
            queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Exit method");
    }
}