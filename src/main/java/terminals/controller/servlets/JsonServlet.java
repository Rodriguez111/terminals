package terminals.controller.servlets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.controller.logic.*;
import terminals.controller.servlets.registrations.ReceiveValidate;
import terminals.models.Terminal;
import terminals.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class JsonServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(JsonServlet.class);
    private final static DepartmentsValidator DEPARTMENTS_VALIDATOR = ValidateDepartments.getINSTANCE();
    private final static TerminalsValidator TERMINALS_VALIDATOR = ValidateTerminals.getINSTANCE();
    private final static RolesValidator ROLES_VALIDATOR = ValidateRoles.getINSTANCE();
    private final static UsersValidator USERS_VALIDATOR = ValidateUsers.getINSTANCE();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Enter method");
        BufferedReader br = req.getReader();
        StringBuilder sb = new StringBuilder();
        String read = "";
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        String requestFromClient = sb.toString();
        System.out.println("requestFromClient = " + requestFromClient);
        JSONObject jsonObject = new JSONObject();

        if (requestFromClient.equals("getListOfDeparts")) {
            List<String> listOfDeparts = DEPARTMENTS_VALIDATOR.findAllDepartments();
            jsonObject.put("listOfDeparts", listOfDeparts);
        } else if (requestFromClient.contains("getTerminalInfo")) {
            JSONObject jsonFromClient = new JSONObject(requestFromClient);
            int id = jsonFromClient.getInt("getTerminalInfo");
            Terminal terminal =  TERMINALS_VALIDATOR.findTerminalById(id);
            jsonObject = new JSONObject(terminal);
        } else if (requestFromClient.equals("getListOfRoles")) {
            List<String> listOfRoles = ROLES_VALIDATOR.findAllRoles();
            jsonObject.put("listOfRoles", listOfRoles);
        } else if (requestFromClient.contains("getUserInfo")) {
            JSONObject jsonFromClient = new JSONObject(requestFromClient);
            int id = jsonFromClient.getInt("getUserInfo");
            User user =  USERS_VALIDATOR.findUserById(id);
            jsonObject = new JSONObject(user);
        } else if (requestFromClient.contains("renameDepartment")) {
            JSONObject jsonFromClient = new JSONObject(requestFromClient);
            String oldDepName = jsonFromClient.getString("renameDepartment");
            String newDepName = jsonFromClient.getString("newDepartmentName");
            String result =  DEPARTMENTS_VALIDATOR.renameDepartment(oldDepName, newDepName);
            jsonObject.put("result", result);
        } else if (requestFromClient.equals("getAllTerminals")) {
            List<Terminal> listOfTerminals = TERMINALS_VALIDATOR.findAllTerminals();
            jsonObject.put("listOfTerminals", listOfTerminals);
        } else if (requestFromClient.equals("getActiveTerminals")) {
            List<Terminal> listOfTerminals = TERMINALS_VALIDATOR.findActiveTerminals();
            jsonObject.put("listOfTerminals", listOfTerminals);
        } else if (requestFromClient.equals("getCountOfAllTerminals")) {
            int all = TERMINALS_VALIDATOR.getCountOfAllTerminals();
            jsonObject.put("countOfAllTerminals", all);
        } else if (requestFromClient.equals("getCountOfActiveTerminals")) {
            int active = TERMINALS_VALIDATOR.getCountOfActiveTerminals();
            jsonObject.put("countOfActiveTerminals", active);
        }

        else if (requestFromClient.equals("getAllUsers")) {
            List<User> listOfUsers = USERS_VALIDATOR.findAllUsers();
            jsonObject.put("listOfUsers", listOfUsers);
        } else if (requestFromClient.equals("getActiveUsers")) {
            List<User> listOfUsers = USERS_VALIDATOR.findActiveUsers();
            jsonObject.put("listOfUsers", listOfUsers);
        } else if (requestFromClient.equals("getCountOfAllUsers")) {
            int all = USERS_VALIDATOR.getCountOfAllUsers();
            jsonObject.put("countOfAllUsers", all);
        } else if (requestFromClient.equals("getCountOfActiveUsers")) {
            int active = USERS_VALIDATOR.getCountOfActiveUsers();
            jsonObject.put("countOfActiveUsers", active);
        }

        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(jsonObject);
        printWriter.flush();
        LOG.info("Exit method");
    }
}
