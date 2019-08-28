package terminals.controller.servlets.registrations;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.controller.logic.RegValidator;
import terminals.controller.logic.TerminalsValidator;
import terminals.controller.logic.ValidateRegs;
import terminals.controller.logic.ValidateTerminals;
import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TerminalInputValidate extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalInputValidate.class);
    private final static RegValidator REGS_VALIDATOR = ValidateRegs.getINSTANCE();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder builder = new StringBuilder();
        String read;
        while ((read = reader.readLine()) != null) {
            builder.append(read);
        }
        String stringFromClient = builder.toString();
        stringFromClient = stringFromClient.replaceAll("\"", "");
        LOG.info("From client: " + stringFromClient);
        JSONObject result = REGS_VALIDATOR.validateTerminalInput(stringFromClient);
        req.setAttribute("serverResponse", result);
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject serverResponse = (JSONObject) req.getAttribute("serverResponse");
        LOG.info("From server: " + serverResponse);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(serverResponse);
        out.flush();
    }


}
