package terminals.controller.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.models.User;
import terminals.storage.DBUser;
import terminals.storage.UserStorage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class ValidateUsers implements UsersValidator {
    private static final Logger LOG = LoggerFactory.getLogger(ValidateUsers.class);
    private final static ValidateUsers INSTANCE = new ValidateUsers();

    private UserStorage userStorage = DBUser.getINSTANCE();

    private ValidateUsers() {
    }

    public static ValidateUsers getINSTANCE() {
        return INSTANCE;
    }

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User checkUserCanLogin(String login, String password) {
        LOG.info("Enter method");
        List<User> list = userStorage.findAllUsers();
        User user = null;
        for (User eachUser : list) {
            if (eachUser.getUserLogin().equals(login) && eachUser.getUserPassword().equals(password)
            && isAdmin(eachUser)) {
                user = eachUser;
                break;
            }
        }
        LOG.info("Exit method");
        return user;
    }

    private boolean isAdmin(User user) {
        return user.getUserRole().equals("root") || user.getUserRole().equals("administrator");
    }

    @Override
    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }


    @Override
    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }


    @Override
    public String addUser(HttpServletRequest request) {
        LOG.info("Enter method");
        String resultMessage;
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String role = request.getParameter("role");
        String department = request.getParameter("department");
        boolean isActive = request.getParameter("isActive")!= null;
        User user = new User(login, password, name, surname, role, department, isActive);
        if (department.equals("")) {
            resultMessage =  userStorage.addUser(user);
        } else {
            resultMessage =  userStorage.addUserWithDepartment(user);
        }
        LOG.info("Exit method");
        return resultMessage;
    }

    @Override
    public String updateUser(HttpServletRequest request) {
        LOG.info("Enter method");
        String result = "Пользователь не существует";
        User user = findUserById(Integer.parseInt(request.getParameter("id")));
        if(user != null) {
        User updatedUser = updateUserFields(user, request);
        if(user.equals(updatedUser)) {
            result = "Нет полей для обновления";
        }
        else if(user.isActive() != updatedUser.isActive() && !user.getTerminalRegId().isEmpty()) {
            result = "Нельзя деактивировать пользователя, пока за ним числится терминал";
        }
        else if(!user.getUserDepartment().equals(updatedUser.getUserDepartment()) && !user.getTerminalRegId().isEmpty()) {
            result = "Нельзя сменить департамент пользователя, пока за ним числится терминал";
        }
        else {
            if (updatedUser.getUserDepartment().equals("")) {
                result = userStorage.updateUser(updatedUser);
            } else {
                result = userStorage.updateUserWithDepartment(updatedUser);
            }
        }
            LOG.info("Exit method");
        }
        return result;
    }

    @Override
    public String deleteUser(HttpServletRequest request) {
        return userStorage.deleteUser(Integer.parseInt(request.getParameter("id")));
    }

    private User updateUserFields (User user, HttpServletRequest request) {
        LOG.info("Enter method");
        User updatedUser = (User)user.clone();
        if(validateField(request.getParameter("login"))) {
            updatedUser.setUserLogin(request.getParameter("login"));
        }
        if(validateField(request.getParameter("password"))) {
            updatedUser.setUserPassword(request.getParameter("password"));
        }
        if(validateField(request.getParameter("name"))) {
            updatedUser.setUserName(request.getParameter("name"));
        }
        if(validateField(request.getParameter("surname"))) {
            updatedUser.setUserSurname(request.getParameter("surname"));
        }
        if(validateField(request.getParameter("role"))) {
            updatedUser.setUserRole(request.getParameter("role"));
        }
        if(request.getParameter("department") != null) {
            updatedUser.setUserDepartment(request.getParameter("department"));
        }
        updatedUser.setActive(request.getParameter("isActive") != null);
        LOG.info("Exit method");
        return updatedUser;
    }

    private boolean validateField(String value) {
        LOG.info("Enter and exit method");
        return value != null && !value.equals("");
    }
}
