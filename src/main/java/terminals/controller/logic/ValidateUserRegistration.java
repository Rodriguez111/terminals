package terminals.controller.logic;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.models.Registration;
import terminals.models.User;
import terminals.sql.DataType;
import terminals.storage.*;

import java.util.HashMap;
import java.util.Map;

public class ValidateUserRegistration implements UserRegistrationValidator {
    private static final Logger LOG = LoggerFactory.getLogger(ValidateUserRegistration.class);
    private static final ValidateUserRegistration INSTANCE = new ValidateUserRegistration();
    private TerminalStorage terminalStorage = DBTerminal.getINSTANCE();
    private RegistrationStorage registrationStorage = DBRegistration.getINSTANCE();
    private UserStorage userStorage = DBUser.getINSTANCE();


    private ValidateUserRegistration() {
    }

    public static ValidateUserRegistration getINSTANCE() {
        return INSTANCE;
    }

    public void setTerminalStorage(TerminalStorage terminalStorage) {
        this.terminalStorage = terminalStorage;
    }

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void setRegistrationStorage(RegistrationStorage registrationStorage) {
        this.registrationStorage = registrationStorage;
    }

    @Override
    public JSONObject validateUserInputForGiving(String stringFromClient) {
        LOG.info("Enter method");
        JSONObject jsonFromClient = new JSONObject(stringFromClient);
        String login = jsonFromClient.getString("userInputLogin");
        int terminalId = jsonFromClient.getInt("terminalId");
        String whoGaveLogin = jsonFromClient.getString("whoGaveLogin");
        int whoGaveId = userStorage.findIdByField("user_login", whoGaveLogin);
        JSONObject result = new JSONObject();
        int userId = userExists(login);
        if (userId == -1) {
            result.put("userNotExists", "Пользователь с таким логином не существует");
        } else {
            String isActive = userStorage.findFieldById(userId, "user_is_active", DataType.BOOLEAN);
            if (isActive.equals("0")) { //если пользователь неактивен
                result.put("userNotActive", "Пользователь " + login + " деактивирован");
            } else if (isActive.equals("1")) {
                int termId = checkOneTerminalInOneHand(userId);
                if (termId != 0 && terminalId != termId) { //если у этого пользователя уже есть другой терминал
                    String regId = terminalStorage.findFieldById(termId, "terminal_reg_id", DataType.STRING);
                    result.put("userAlreadyHaveTerminal", "За пользователем уже зарегистрирован терминал " + regId);
                } else {
                    String departmentResult = checkDepartmentAffiliation(userId, terminalId);
                    if (departmentResult.equals("OK")) { //если департаменты совпадают
                        Registration entry = new Registration(terminalId, userId, whoGaveId);
                        registrationStorage.addEntry(entry);
                        userStorage.addTerminalToUser(userId, terminalId);
                        terminalStorage.addUserToTerminal(terminalId, userId);
                        result = createJsonAfterTerminalReceivedOrGiven(userId);
                        result.put("terminalGivingSuccess", "OK");
                    } else {
                        result.put("departmentsNotMatch", departmentResult);
                    }
                }
            }
        }
        LOG.info("Exit method");
        return result;
    }

    private JSONObject createJsonAfterTerminalReceivedOrGiven(int userId) {
        JSONObject result = new JSONObject();
        int totalAmountOfTerminals = terminalStorage.countOfTerminals(null);
        int amountOfInactiveTerminals = terminalStorage.countOfTerminals("inactive");
        int amountOfGivenTerminals = terminalStorage.countOfTerminals("given");
        int activeTerminalsRemain = totalAmountOfTerminals - amountOfGivenTerminals - amountOfInactiveTerminals;
        result = getUserInfoById(userId);
        result.put("totalAmountOfTerminals", totalAmountOfTerminals);
        result.put("amountOfInactiveTerminals", amountOfInactiveTerminals);
        result.put("amountOfGivenTerminals", amountOfGivenTerminals);
        result.put("activeTerminalsRemain", activeTerminalsRemain);
        return result;
    }

    @Override
    public JSONObject validateUserInputForReceiving(String stringFromClient) {
        LOG.info("Enter method");
        JSONObject jsonFromClient = new JSONObject(stringFromClient);
        String login = jsonFromClient.getString("userInputLogin");
        int terminalId = jsonFromClient.getInt("terminalId");
        String whoReceivedLogin = jsonFromClient.getString("whoReceivedLogin");
        int whoReceivedId = userStorage.findIdByField("user_login", whoReceivedLogin);
        JSONObject result = new JSONObject();
        int userId = userExists(login);
        if (userId == -1) {
            result.put("userNotExists", "Пользователь с таким логином не существует");
        } else {
            int termId = checkOneTerminalInOneHand(userId);
            if (termId == 0) { //пользователь существует, но у него нет терминала
                result.put("doNotHaveTerminal", "Этот пользователь не брал терминал");
            } else if (terminalId != termId) { //если у этого пользователя уже есть другой терминал
                String regId = terminalStorage.findFieldById(termId, "terminal_reg_id", DataType.STRING);
                result.put("userNotMatch", "За этим пользователем зарегистрирован другой терминал: " + regId);
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(userId));
                map.put("terminal_id", String.valueOf(terminalId));
                int regId = registrationStorage.findIdByField(map);
                if (regId != -1) {
                    registrationStorage.updateEntry(regId, whoReceivedId);
                    userStorage.removeTerminalFromUser(userId);
                    terminalStorage.removeUserFromTerminal(terminalId);
                    result = createJsonAfterTerminalReceivedOrGiven(userId);
                    result.put("terminalReceivingSuccess", "OK");
                } else {
                    result.put("fatalErrorRecordNotFound", "Fatal error: запись не найдена");
                }
            }
        }
        return result;
    }

    private int checkOneTerminalInOneHand(int userId) {
        return userStorage.checkOneTerminalInOneHand(userId);
    }

    ;

    private String checkDepartmentAffiliation(int userId, int terminalId) {
        String result = "OK";
        String userDepartmentId = userStorage.findFieldById(userId, "user_department_id", DataType.STRING);
        String terminalDepartment = terminalStorage.findFieldById(terminalId, "terminal_department_id", DataType.STRING);

        if (((!userDepartmentId.equals("") && !terminalDepartment.equals(""))
                && !userDepartmentId.equals(terminalDepartment)) ||
                (userDepartmentId.equals("") && !terminalDepartment.equals(""))) {
            result = "Терминал принадлежит другому департаменту";
        }
        return result;
    }

    private int userExists(String userLogin) {
        return userStorage.findIdByField("user_login", userLogin);
    }

    private JSONObject getUserInfoById(int userId) {
        User user = userStorage.findUserById(userId);
        JSONObject json = new JSONObject();
        json.put("login", user.getUserLogin());
        json.put("name", user.getUserName());
        json.put("surname", user.getUserSurname());
        return json;
    }
}
