package terminals.controller.servlets.users;

import terminals.controller.logic.UsersValidator;
import terminals.controller.logic.ValidateUsers;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateUser extends HttpServlet {
    private final static UsersValidator USERS_VALIDATOR = ValidateUsers.getINSTANCE();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String result = USERS_VALIDATOR.updateUser(req);
        if (result.equals("OK")) {
            resp.sendRedirect(req.getContextPath() + "/users");

        }  else {
            req.setAttribute("sysMessage", result);
            req.getRequestDispatcher(Pages.UPDATE_USER_JSP.page).forward(req, resp);
        }
    }
}
