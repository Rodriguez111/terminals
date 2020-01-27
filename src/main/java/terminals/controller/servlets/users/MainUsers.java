package terminals.controller.servlets.users;

import terminals.controller.logic.*;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainUsers extends HttpServlet {
    private final static UsersValidator USERS_VALIDATOR = ValidateUsers.getINSTANCE();
    private final static DepartmentsValidator DEPARTMENTS_VALIDATOR = ValidateDepartments.getINSTANCE();
    private final static RolesValidator ROLES_VALIDATOR = ValidateRoles.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listOfUsers", USERS_VALIDATOR.findAllUsers());
        req.setAttribute("listOfDeparts", DEPARTMENTS_VALIDATOR.findAllDepartments());
        req.setAttribute("listOfRoles", ROLES_VALIDATOR.findAllRoles());
        req.getRequestDispatcher(Pages.MAIN_USERS_JSP.page).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
