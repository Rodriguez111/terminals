package terminals.controller.logic;

import org.json.JSONObject;

public interface UserRegistrationValidator {
   JSONObject validateUserInputForGiving(String stringFromClient);

   JSONObject validateUserInputForReceiving(String stringFromClient);
}
