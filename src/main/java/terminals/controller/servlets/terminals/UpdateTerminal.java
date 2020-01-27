package terminals.controller.servlets.terminals;

import terminals.controller.logic.TerminalsValidator;
import terminals.controller.logic.UsersValidator;
import terminals.controller.logic.ValidateTerminals;
import terminals.controller.logic.ValidateUsers;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateTerminal extends HttpServlet {
    private final static TerminalsValidator TERMINALS_VALIDATOR = ValidateTerminals.getINSTANCE();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = TERMINALS_VALIDATOR.updateTerminal(req);
        if(result.equals("OK")) {
            resp.sendRedirect(req.getContextPath() + "/terminals");
        } else {
            req.setAttribute("sysMessage", result);
            req.getRequestDispatcher(Pages.UPDATE_TERMINAL_JSP.page).forward(req, resp);
        }

    }
}
