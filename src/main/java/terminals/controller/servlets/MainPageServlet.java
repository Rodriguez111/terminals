package terminals.controller.servlets;

import org.json.JSONObject;
import terminals.controller.logic.*;
import terminals.models.Pages;
import terminals.models.Registration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainPageServlet extends HttpServlet {

    private final static RegValidator REGS_VALIDATOR = ValidateRegs.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> map = REGS_VALIDATOR.filterButtonsCondition();
        JSONObject resultMap = new JSONObject(map);
        req.setAttribute("listOfRegs", REGS_VALIDATOR.findAllEntriesForTheLastDay());
        req.setAttribute("filterButtonsCondition", resultMap);
        req.getRequestDispatcher(Pages.MAIN_JSP.page).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
