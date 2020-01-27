package terminals.controller.servlets.terminals;

import terminals.controller.logic.*;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainTerminals extends HttpServlet {

    private final static TerminalsValidator TERMINALS_VALIDATOR = ValidateTerminals.getINSTANCE();
    private final static DepartmentsValidator DEPARTMENTS_VALIDATOR = ValidateDepartments.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listOfTerminals", TERMINALS_VALIDATOR.findAllTerminals());
        req.setAttribute("listOfDeparts", DEPARTMENTS_VALIDATOR.findAllDepartments());
        req.getRequestDispatcher(Pages.MAIN_TERMINALS_JSP.page).forward(req, resp);
        long end = System.currentTimeMillis();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
