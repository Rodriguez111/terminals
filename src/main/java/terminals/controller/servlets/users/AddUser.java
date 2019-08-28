package terminals.controller.servlets.users;

import terminals.controller.logic.UsersValidator;
import terminals.controller.logic.ValidateUsers;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddUser extends HttpServlet {

    private final static UsersValidator USERS_VALIDATOR = ValidateUsers.getINSTANCE();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String result =  USERS_VALIDATOR.addUser(req);
      if(result.equals("Логин существует")) {
          req.setAttribute("sysMessage", "Такой логин уже существует.");
          req.getRequestDispatcher(Pages.ADD_USER_JSP.page).forward(req, resp);

      } else {
          resp.sendRedirect(req.getContextPath() + "/users");
      }

    }
}
