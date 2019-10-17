package terminals.controller.logic;

import terminals.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UsersValidator {

    User checkUserCanLogin(String login, String password);

    List<User> findAllUsers();

    List<User> findActiveUsers();

    int getCountOfAllUsers();

    int getCountOfActiveUsers();

    User findUserById(int id);

    String addUser(HttpServletRequest request);

    String updateUser(HttpServletRequest request);

    String deleteUser(HttpServletRequest request);
}
