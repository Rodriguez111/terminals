package terminals.storage;

import terminals.models.User;
import terminals.sql.DataType;

import java.util.List;

public interface UserStorage {
    List<User> findAllUsers();

    User findUserById(int id);

    int findIdByField(String fieldName, String value);

    int terminalIsGivenToUser(int terminalId);

    int checkOneTerminalInOneHand(int userId);

    String findFieldById(int id, String fieldName, DataType dataType);

    String addUser(User user);

    String addUserWithDepartment(User user);

    String updateUser(User user);

    String updateUserWithDepartment(User user);

    String deleteUser(int id);

    void addTerminalToUser(int userId, int terminalId);

    void removeTerminalFromUser(int userId);

}
