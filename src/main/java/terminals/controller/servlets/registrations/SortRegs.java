package terminals.controller.servlets.registrations;

import org.json.JSONObject;
import terminals.controller.logic.RegValidator;
import terminals.controller.logic.ValidateRegs;
import terminals.models.Pages;
import terminals.models.Registration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SortRegs extends HttpServlet {
    private final static RegValidator REGS_VALIDATOR = ValidateRegs.getINSTANCE();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<List<Registration>, Map<String, String>> sortResult = REGS_VALIDATOR.sortEntries(req);
        Map.Entry<List<Registration>, Map<String, String>> entry = sortResult.entrySet().iterator().next();

        Map<String, String> map = entry.getValue();
        JSONObject resultMap = new JSONObject(map);

        req.setAttribute("listOfRegs", entry.getKey());
        req.setAttribute("filterButtonsCondition", resultMap);
        req.getRequestDispatcher(Pages.MAIN_JSP.page).forward(req, resp);

    }
}
