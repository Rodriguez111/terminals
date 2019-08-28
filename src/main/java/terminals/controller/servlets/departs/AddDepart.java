package terminals.controller.servlets.departs;

import terminals.controller.logic.DepartmentsValidator;
import terminals.controller.logic.ValidateDepartments;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddDepart extends HttpServlet {
    private final static DepartmentsValidator DEPARTMENTS_VALIDATOR = ValidateDepartments.getINSTANCE();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = DEPARTMENTS_VALIDATOR.addDepartment(req);
        if (result.equals("Департамент существует")) {
            req.setAttribute("sysMessage", "Такой департамент уже существует.");
            req.getRequestDispatcher(Pages.ADD_DEPART_JSP.page).forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/departs");
        }
    }
}
