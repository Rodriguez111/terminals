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

public class AddTerminal extends HttpServlet {
    private final static TerminalsValidator TERMINALS_VALIDATOR = ValidateTerminals.getINSTANCE();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result =  TERMINALS_VALIDATOR.addTerminal(req);
        if(!result.equals("OK")) {
            req.setAttribute("sysMessage", result);
            req.getRequestDispatcher(Pages.ADD_TERMINAL_JSP.page).forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/terminals");
        }
    }
}
