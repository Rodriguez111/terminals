package terminals.controller.logic;

import org.json.JSONObject;
import terminals.models.Registration;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RegValidator {

    List<Registration> findAllEntriesForTheLastDay();

    List<Registration> findEntriesByFilter(HttpServletRequest request);

    Map<List<Registration>, Map<String, String>> sortEntries(HttpServletRequest request);

    Map<String, String> filterButtonsCondition ();

    JSONObject validateTerminalInput(String terminalSerialId);

    JSONObject validateUserInputForGiving(String stringFromClient);

    JSONObject validateUserInputForReceiving(String stringFromClient);

    int countOfTerminals(String whatToCount);

}
