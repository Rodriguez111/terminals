package terminals.controller.servlets.registrations;

import terminals.controller.logic.*;
import terminals.models.Pages;
import terminals.models.Registration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FilterRegs extends HttpServlet {

    private final static RegValidator REGS_VALIDATOR = ValidateRegs.getINSTANCE();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("listOfRegs", REGS_VALIDATOR.findEntriesByFilter(req));

        req.getRequestDispatcher(Pages.MAIN_JSP.page).forward(req, resp);


    }
}
