package terminals.controller.servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession();
        if (!request.getRequestURI().contains("/login")) {
            if (!checkRole(session)) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        }
        if (request.getRequestURI().contains("/login") && checkRole(session)) {
            response.sendRedirect(request.getContextPath() + "/main");
            return;
        }
        filterChain.doFilter(request, response);

    }


    private boolean checkRole(HttpSession session) {
       return  session.getAttribute("role") != null &&
               (session.getAttribute("role").equals("root")
                       || session.getAttribute("role").equals("administrator"));
    }

}
