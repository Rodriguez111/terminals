package terminals.controller.logic;

import org.json.JSONObject;
import terminals.models.User;
import terminals.sql.DataType;
import terminals.storage.*;

import java.util.HashMap;
import java.util.Map;

public class ValidateTerminalRegistration implements TerminalRegistrationValidator {
   private static final ValidateTerminalRegistration INSTANCE = new ValidateTerminalRegistration();
    private TerminalStorage terminalStorage = DBTerminal.getINSTANCE();
    private UserStorage userStorage = DBUser.getINSTANCE();


    private ValidateTerminalRegistration() {
    }

    public static ValidateTerminalRegistration getINSTANCE() {
        return INSTANCE;
    }

    public void setTerminalStorage(TerminalStorage terminalStorage) {
        this.terminalStorage = terminalStorage;
    }

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public JSONObject validateTerminalInput(String terminalInvId) {
        JSONObject result = new JSONObject();
        int terminalId = terminalExists(terminalInvId);

      if(terminalId == -1) {
          result.put("terminalNotExists", "Терминал с таким номером не существует");
      } else {
          String regId = terminalStorage.findFieldById(terminalId, "terminal_reg_id", DataType.STRING);
          String isActive = terminalStorage.findFieldById(terminalId, "terminal_is_active", DataType.BOOLEAN);
            if (isActive.equals("0")) {
                result.put("terminalNotActive", "Терминал "+ regId + " деактивирован");
            } else if (isActive.equals("1")) {
              int userId = terminalIsGivenToUser(terminalId);
              if (userId == -1) { //Если этот терминал не выдан
                  result.put("terminalIsReady", "OK"); //тогда терминал идет на выдачу
                  result.put("terminalRegId", regId);
                  result.put("terminalId", terminalId);
              }   else { //иначе идет на прием
                  String userLogin = userStorage.findFieldById(userId, "user_login", DataType.STRING);
                  result = getUserInfoById(userId);
                  result.put("terminalRegId", regId);
                  result.put("terminalId", terminalId);
                  result.put("userLogin", userLogin);
              }
          }
      }
        return result;
    }

    private int terminalExists(String terminalInvId) {
        Map<String, String> mapOfFields = new HashMap<>();
        mapOfFields.put("terminal_inventory_id", terminalInvId);
        return terminalStorage.findIdByField(mapOfFields);
    }

    private int terminalIsGivenToUser(int terminalId) {
        return userStorage.terminalIsGivenToUser(terminalId);
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
