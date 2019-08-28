package terminals.controller.servlets.terminals;

import terminals.controller.logic.TerminalsValidator;
import terminals.controller.logic.UsersValidator;
import terminals.controller.logic.ValidateTerminals;
import terminals.controller.logic.ValidateUsers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteTerminal extends HttpServlet {
    private final static TerminalsValidator TERMINALS_VALIDATOR = ValidateTerminals.getINSTANCE();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = TERMINALS_VALIDATOR.deleteTerminal(req);
        if (result.equals("OK")) {
            resp.sendRedirect(req.getContextPath() + "/terminals");
        } else {
            HttpSession session = req.getSession(false);
            session.setAttribute("sysMessage", result);
            resp.sendRedirect(req.getContextPath() + "/terminals");
        }
    }
}
