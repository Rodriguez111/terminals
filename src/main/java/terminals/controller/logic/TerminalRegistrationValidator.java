package terminals.controller.logic;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface TerminalRegistrationValidator {
   JSONObject validateTerminalInput(String terminalSerialId);
}
