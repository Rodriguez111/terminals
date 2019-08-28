package terminals.controller.servlets.departs;

import terminals.controller.logic.DepartmentsValidator;
import terminals.controller.logic.UsersValidator;
import terminals.controller.logic.ValidateDepartments;
import terminals.controller.logic.ValidateUsers;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MainDeparts extends HttpServlet {
    private final static DepartmentsValidator DEPARTMENTS_VALIDATOR = ValidateDepartments.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> listOfDepartments = DEPARTMENTS_VALIDATOR.findAllDepartments();
        listOfDepartments.remove("");
       req.setAttribute("listOfDepartments", listOfDepartments);
       req.getRequestDispatcher(Pages.MAIN_DEPS_JSP.page).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
