package terminals.controller.servlets.registrations;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.controller.logic.RegValidator;
import terminals.controller.logic.ValidateRegs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ReceiveValidate extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ReceiveValidate.class);
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
        LOG.info("From client: " + stringFromClient);
        JSONObject result =  REGS_VALIDATOR.validateUserInputForReceiving(stringFromClient);

        LOG.info("From server: " + result);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(result);
        out.flush();
    }

}
