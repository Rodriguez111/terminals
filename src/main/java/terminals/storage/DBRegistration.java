package terminals.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.controller.logic.ValidateRegs;
import terminals.models.Registration;
import terminals.sql.DataType;
import terminals.sql.QueryManager;
import terminals.sql.SQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBRegistration implements RegistrationStorage {
//    private static final Logger LOG = LoggerFactory.getLogger(DBRegistration.class);
    private final static DBUser USER_STORAGE = DBUser.getINSTANCE();
    private final static DBTerminal TERMINAL_STORAGE = DBTerminal.getINSTANCE();
    private final static DBRegistration INSTANCE = new DBRegistration();
    private DBRegistration() {
    }


    public static DBRegistration getINSTANCE() {
        return INSTANCE;
    }


    public List<Registration> findEntriesByFilter(Map<String, String> parameters) {
//        LOG.info("Enter method");
        List<Registration> resultList = new ArrayList<>();
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = composeFilterQuery(parameters);
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(query, params, ps -> {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                resultList.add(composeEntryFromDBResultSet(resultSet));
            }
        });
//        LOG.info("Exit method");
        return resultList;
    }

    private String composeFilterQuery(Map<String, String> parameters) {
//        LOG.info("Enter method");
        StringBuilder sb = new StringBuilder("SELECT * FROM registrations WHERE ");

        if(parameters.containsKey("regIdFilter")) {
            String terminalPart = "terminal_id IN (SELECT terminal_id FROM terminals WHERE terminal_reg_id LIKE '%"+ parameters.get("regIdFilter") + "%') AND ";
            sb.append(terminalPart);
        }
        if(parameters.containsKey("loginFilter") || parameters.containsKey("fullNameFilter")) {
            sb.append("user_id IN (");

            StringBuilder stringForUserId = new StringBuilder("SELECT user_id FROM users WHERE ");

            String login = parameters.containsKey("loginFilter") ? "user_login LIKE '%" + parameters.get("loginFilter") + "%' AND " : "";
            String fullName = parameters.containsKey("fullNameFilter") ? "user_surname LIKE '%"
                    + parameters.get("fullNameFilter") + "%' OR user_name LIKE '%" + parameters.get("fullNameFilter") + "%' AND " : "";

            stringForUserId.append(login).append(fullName);

            String userPart = stringForUserId.toString();

            if(userPart.endsWith("AND ")) {
                userPart = userPart.substring(0, userPart.length() - 5) ;
            }
            if(userPart.endsWith("WHERE ")) {
                userPart = userPart.substring(0, userPart.length() - 7) ;
            }
            sb.append(userPart);
            sb.append(") AND ");
        }

        if(parameters.containsKey("whoGaveFilter")) {
            sb.append("admin_gave_id IN (");
            StringBuilder adminGaveId = new StringBuilder("SELECT user_id FROM users WHERE ");
            String fullName =  "user_surname LIKE '%" + parameters.get("whoGaveFilter")
                    + "%' OR user_name LIKE '%" + parameters.get("whoGaveFilter") + "%' ";
            adminGaveId.append(fullName);
            String adminGavePart = adminGaveId.toString();
            if(adminGavePart.endsWith("WHERE ")) {
                adminGavePart = adminGavePart.substring(0, adminGavePart.length() - 7) ;
            }
            sb.append(adminGavePart);
            sb.append(") AND ");
        }

        if(parameters.containsKey("whoReceivedFilter")) {
            sb.append("admin_received_id IN (");
            StringBuilder adminReceivedId = new StringBuilder("SELECT user_id FROM users WHERE ");
            String fullName =  "user_surname LIKE '%" + parameters.get("whoReceivedFilter")
                    + "%' OR user_name LIKE '%" + parameters.get("whoReceivedFilter") + "%' ";
            adminReceivedId.append(fullName);
            String adminReceivedPart = adminReceivedId.toString();
            if(adminReceivedPart.endsWith("WHERE ")) {
                adminReceivedPart = adminReceivedPart.substring(0, adminReceivedPart.length() - 7) ;
            }
            sb.append(adminReceivedPart);
            sb.append(") AND ");
        }

        if(parameters.containsKey("startDateFilterFrom") || parameters.containsKey("startDateFilterTo")
                ||  parameters.containsKey("endDateFilterFrom") || parameters.containsKey("endDateFilterTo")) {
            StringBuilder stringForDate = new StringBuilder();
            String startDateFilterFrom = parameters.containsKey("startDateFilterFrom") ? "record_start_date > '" + parameters.get("startDateFilterFrom") + "' AND " : "";
            String startDateFilterTo = parameters.containsKey("startDateFilterTo") ? "record_start_date < '" + parameters.get("startDateFilterTo") + "' AND " : "";
            String endDateFilterFrom = parameters.containsKey("endDateFilterFrom") ? "record_end_date > '" + parameters.get("endDateFilterFrom") + "' AND " : "";
            String endDateFilterTo = parameters.containsKey("endDateFilterTo") ? "record_end_date < '" + parameters.get("endDateFilterTo") + "' AND " : "";
            stringForDate.append(startDateFilterFrom).append(startDateFilterTo).append(endDateFilterFrom).append(endDateFilterTo);
            String datePart = stringForDate.toString();

            if(datePart.endsWith("AND ")) {
                datePart = datePart.substring(0, datePart.length() - 5) ;
            }

            sb.append(datePart);
        }
        String query =  sb.toString();

        if(query.endsWith("AND ")) {
            query = query.substring(0, query.length() - 5) ;
        }
        if(query.endsWith("WHERE ")) {
            query = query.substring(0, query.length() - 7) ;
        }
//        LOG.info("Exit method");
        return query;
    }

    @Override
    public int findIdByField(Map<String, String> parameters) {
//        LOG.info("Enter method");
        int result = -1;
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query =  queryManager.queryComposerForExactSearch("SELECT record_id FROM registrations WHERE ", parameters);
        query += " AND record_finish_date IS NULL";
        List<Object> params = new ArrayList<>();
        try {
            result = queryManager.runQuery(query, params, ps -> {
                int id = -1;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt("record_id");
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
    public void addEntry(Registration entry) {
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "INSERT INTO registrations "
                + "(user_id, terminal_id, admin_gave_id) "
                + "VALUES (?, ?, ?);";
        int userId = entry.getUserId();
        int terminalId = entry.getTerminalId();
        int whoGaveId = entry.getWhoGaveId();
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(terminalId);
        params.add(whoGaveId);
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
    public void updateEntry(int id, int whoReceivedId) {
//        LOG.info("Enter method");
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "UPDATE registrations SET admin_received_id = ?, record_finish_date = datetime(CURRENT_TIMESTAMP, 'localtime')  WHERE record_id=?";
        List<Object> params = new ArrayList<>();
        params.add(whoReceivedId);
        params.add(id);
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
    public List<Registration> findAllEntriesForTheLastDay(String yesterday) {
//        LOG.info("Enter method");
        List<Registration> resultList = new ArrayList<>();
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM registrations WHERE record_start_date > ?";
        List<Object> params = new ArrayList<>();
        params.add(yesterday);
        queryManager.runQuery(query, params, ps -> {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                resultList.add(composeEntryFromDBResultSet(resultSet));
            }
        });
//        LOG.info("Exit method");
        return resultList;
    }


    private Registration composeEntryFromDBResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        int terminalId = resultSet.getInt("terminal_id");
        int whoGaveId = resultSet.getInt("admin_gave_id");
        int whoReceivedId = resultSet.getInt("admin_received_id");
        int recordId = resultSet.getInt("record_id");
        String userLogin = USER_STORAGE.findFieldById(userId, "user_login", DataType.STRING);
        String userName = USER_STORAGE.findFieldById(userId, "user_name", DataType.STRING);
        String userSurname = USER_STORAGE.findFieldById(userId, "user_surname", DataType.STRING);
        String userFullName = userSurname + " " + userName;
        String whoGaveSurname = USER_STORAGE.findFieldById(whoGaveId, "user_surname", DataType.STRING);
        String whoGaveName = USER_STORAGE.findFieldById(whoGaveId, "user_name", DataType.STRING);
        String whoGave = whoGaveSurname + " " + whoGaveName;
        String whoReceivedSurname = USER_STORAGE.findFieldById(whoReceivedId, "user_surname", DataType.STRING);
        String whoReceivedName = USER_STORAGE.findFieldById(whoReceivedId, "user_name", DataType.STRING);
        String whoReceived = whoReceivedSurname.isEmpty() && whoReceivedName.isEmpty() ? "" : whoReceivedSurname + " " + whoReceivedName;
        String terminalRegistrationId = TERMINAL_STORAGE.findFieldById(terminalId, "terminal_reg_id", DataType.STRING);
        String startDate = resultSet.getString("record_start_date");
        String finishDate = resultSet.getString("record_finish_date");
        String endDate =  finishDate == null ? "" :  finishDate;
        Registration registration = new Registration(userLogin, userFullName, whoGave,
                whoReceived, terminalRegistrationId, startDate, endDate);
        registration.setRecordId(recordId);
        return registration;
    }
}