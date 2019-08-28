package terminals.controller.servlets;

import terminals.models.Pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Selector extends HttpServlet {
    Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> dispatcher = new HashMap<>();

    @Override
    public void init() throws ServletException {
        initDispatcher();
    }

    private void initDispatcher() {
        dispatcher.put("add_user", (request, response) -> {
            try {
                request.getRequestDispatcher(Pages.ADD_USER_JSP.page).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        });
        dispatcher.put("update_user", (request, response) -> {
            try {
                request.getRequestDispatcher(Pages.UPDATE_USER_JSP.page).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        });
        dispatcher.put("add_depart", (request, response) -> {
            try {
                request.getRequestDispatcher(Pages.ADD_DEPART_JSP.page).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        });
        dispatcher.put("add_terminal", (request, response) -> {
            try {
                request.getRequestDispatcher(Pages.ADD_TERMINAL_JSP.page).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        });
        dispatcher.put("update_terminal", (request, response) -> {
            try {
                request.getRequestDispatcher(Pages.UPDATE_TERMINAL_JSP.page).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        });
        dispatcher.put("generate_barcode", (request, response) -> {
            try {
                request.getRequestDispatcher(Pages.GENERATE_BARCODE_JSP.page).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        dispatcher.get(req.getParameter("action")).accept(req, resp);
    }

}
