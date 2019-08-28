package terminals.controller.servlets.departs;

import terminals.controller.logic.DepartmentsValidator;
import terminals.controller.logic.ValidateDepartments;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteDepart extends HttpServlet {
    private final static DepartmentsValidator DEPARTMENTS_VALIDATOR = ValidateDepartments.getINSTANCE();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String result = DEPARTMENTS_VALIDATOR.deleteDepartment(req);
      if (result.equals("OK")) {
          resp.sendRedirect(req.getContextPath() + "/departs");
      } else {
          HttpSession session = req.getSession(false);
          session.setAttribute("sysMessage", result);
          resp.sendRedirect(req.getContextPath() + "/departs");
      }
    }
}
