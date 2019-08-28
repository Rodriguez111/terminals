package terminals.controller.logic.fakestorage;

import terminals.models.Terminal;
import terminals.models.User;
import terminals.sql.DataType;
import terminals.storage.TerminalStorage;
import terminals.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserFakeDb implements UserStorage {

    private final static AtomicInteger COUNT = new AtomicInteger();
    private TerminalFakeDb terminalStorage;
    private DepartmentFakeDb departmentFakeDb;

    private final Map<Integer, User> users = new TreeMap<>();

    public void setTerminalStorage(TerminalFakeDb terminalStorage) {
        this.terminalStorage = terminalStorage;
    }

    public void setDepartmentFakeDb(DepartmentFakeDb departmentFakeDb) {
        this.departmentFakeDb = departmentFakeDb;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(int id) {
        return users.get(id);
    }

    @Override
    public int findIdByField(String fieldName, String value) {
        int result = -1;
        for(Map.Entry<Integer, User> eachEntry : users.entrySet()) {
            if (fieldName.equals("user_login")) {
                if(eachEntry.getValue().getUserLogin().equals(value)) {
                    result =  eachEntry.getKey();
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public int terminalIsGivenToUser(int terminalId) {
        int result = -1;
        for (Map.Entry<Integer, User> eachEntry : users.entrySet()) {
            if(eachEntry.getValue().getTerminalRegId() != null
                    && eachEntry.getValue().getTerminalRegId().equals(String.valueOf(terminalId))) {
                result = eachEntry.getKey();
            }
        }
        return result;
    }

    @Override
    public int checkOneTerminalInOneHand(int userId) {
        int result = 0;
        User user = users.get(userId);
            if (user.getTerminalRegId() != null) {
                result = terminalStorage.findIdByRegId(user.getTerminalRegId());
            }
        return result;
    }

    @Override
    public String findFieldById(int id, String fieldName, DataType dataType) {
        String result = "";
        User user = users.get(id);
        if (fieldName.equals("user_login")) {
            result =  user.getUserLogin();
        }
        if (fieldName.equals("user_is_active")) {
            result =  user.isActive()? "1" : "0";
        }
        if (fieldName.equals("terminal_reg_id")) {
            result =  user.getTerminalRegId();
        }
        if (fieldName.equals("user_department_id")) {
            String department = user.getUserDepartment();
            result = String.valueOf(departmentFakeDb.findIdByDepartment(department));
        }
        return result;
    }

    @Override
    public String addUser(User user) {
        User user1 = new User(user.getUserLogin(), user.getUserPassword(), user.getUserName(), user.getUserSurname(),
                user.getUserRole(), user.getUserDepartment(), "", user.isActive());
        users.put(COUNT.incrementAndGet(), user1);
        return "OK";
    }

    @Override
    public String addUserWithDepartment(User user) {
        User user1 = new User(user.getUserLogin(), user.getUserPassword(), user.getUserName(), user.getUserSurname(),
                user.getUserRole(), user.getUserDepartment(), "", user.isActive());
        users.put(COUNT.incrementAndGet(), user1);
        return "OK";
    }

    @Override
    public String updateUser(User user) {
        users.put(user.getId(), user);
        return "OK";
    }

    @Override
    public String updateUserWithDepartment(User user) {
        users.put(user.getId(), user);
        return "OK";
    }

    @Override
    public String deleteUser(int id) {
        users.remove(id);
        return "OK";
    }

    @Override
    public void addTerminalToUser(int userId, int terminalId) {

    }

    @Override
    public void removeTerminalFromUser(int userId) {

    }
}
