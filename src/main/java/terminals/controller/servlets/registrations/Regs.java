package terminals.controller.servlets.registrations;

import terminals.controller.logic.*;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Regs extends HttpServlet {

    private final static RegValidator REG_VALIDATOR = ValidateRegs.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalAmountOfTerminals = REG_VALIDATOR.countOfTerminals(null);
        int amountOfInactiveTerminals = REG_VALIDATOR.countOfTerminals("inactive");
        int amountOfGivenTerminals = REG_VALIDATOR.countOfTerminals("given");
        int activeTerminalsRemain = totalAmountOfTerminals - amountOfGivenTerminals - amountOfInactiveTerminals;

        req.setAttribute("totalAmountOfTerminals", totalAmountOfTerminals);
        req.setAttribute("amountOfInactiveTerminals", amountOfInactiveTerminals);
        req.setAttribute("amountOfGivenTerminals", amountOfGivenTerminals);
        req.setAttribute("activeTerminalsRemain", activeTerminalsRemain);

        req.getRequestDispatcher(Pages.MAIN_REGS_JSP.page).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
