package terminals.models;

import java.util.Objects;

public class User implements Cloneable {
    private int id;
    private String userLogin;
    private String userPassword;
    private String userName;
    private String userSurname;
    private String userRole;
    private String userDepartment;
    private String terminalRegId;
    private boolean isActive;
    private String createDate;
    private String lastUpdateDate;

    public User(String userLogin, String userPassword, String userName, String userSurname, String userRole,
                String userDepartment, String terminalRegId, boolean isActive) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userRole = userRole;
        this.userDepartment = userDepartment;
        this.terminalRegId = terminalRegId;
        this.isActive = isActive;
    }

    public User(String userLogin, String userPassword, String userName, String userSurname, String userRole,
                String userDepartment, boolean isActive) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userRole = userRole;
        this.userDepartment = userDepartment;
        this.isActive = isActive;
    }



    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getTerminalRegId() {
        return terminalRegId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive &&
                userLogin.equals(user.userLogin) &&
                userPassword.equals(user.userPassword) &&
                userName.equals(user.userName) &&
                userSurname.equals(user.userSurname) &&
                userRole.equals(user.userRole) &&
                Objects.equals(userDepartment, user.userDepartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLogin, userPassword, userName, userSurname, userRole, userDepartment, isActive);
    }

    @Override
    public Object clone()  {
        User user = null;
        try {
            user = (User)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }
}
