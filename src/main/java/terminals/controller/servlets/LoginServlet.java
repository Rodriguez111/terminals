package terminals.controller.servlets;

import terminals.controller.logic.ValidateUsers;
import terminals.controller.logic.UsersValidator;
import terminals.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final static UsersValidator VALIDATOR = ValidateUsers.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/terminals/viewer/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("password");
        User user = VALIDATOR.checkUserCanLogin(login, pass);
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("name", user.getUserName());
            session.setAttribute("surname", user.getUserSurname());
            session.setAttribute("login", login);
            session.setAttribute("role", user.getUserRole());
            resp.sendRedirect(req.getContextPath() + "/main");
        } else {
            req.setAttribute("errorMessage", "Неверный логин/пароль");
            doGet(req, resp);
        }
    }
}
