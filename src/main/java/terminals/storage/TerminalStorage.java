package terminals.storage;

import terminals.models.Terminal;
import terminals.models.User;
import terminals.sql.DataType;

import java.util.List;
import java.util.Map;

public interface TerminalStorage {
    List<Terminal> findAllTerminals();

    Terminal findTerminalById(int id);

    int findIdByRegId(String regId);

    String findFieldById(int id, String fieldName, DataType dataType);

    String addTerminal(Terminal terminal);

    String addTerminalWithDepartment(Terminal terminal);

    String updateTerminal(Terminal terminal);

    String updateTerminalWithDepartment(Terminal terminal);

    String deleteTerminal(int id);

    int findIdByField(Map<String, String> parameters);

    int countOfTerminals(String whatToCount);

    void addUserToTerminal(int terminalId, int userId);

    void removeUserFromTerminal(int terminalId);
}
