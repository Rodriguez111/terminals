package terminals.controller.logic;

import terminals.models.Terminal;
import terminals.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TerminalsValidator {

    List<Terminal> findAllTerminals();

    List<Terminal> findActiveTerminals();

    int getCountOfAllTerminals();

    int getCountOfActiveTerminals();

    Terminal findTerminalById(int id);

    String addTerminal(HttpServletRequest request);

    String updateTerminal(HttpServletRequest request);

    String deleteTerminal(HttpServletRequest request);
}
