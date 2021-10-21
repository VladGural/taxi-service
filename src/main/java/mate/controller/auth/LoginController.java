package mate.controller.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mate.exception.AuthenticationException;
import mate.lib.Injector;
import mate.model.Driver;
import mate.service.AuthenticationService;

public class LoginController extends HttpServlet {
    private static final String AUTH_ATT = "driver_id";
    private static final Injector injector = Injector.getInstance("mate");
    private final AuthenticationService authenticationService =
              (AuthenticationService) injector.getInstance(AuthenticationService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            Driver driver = authenticationService.login(login, password);
            HttpSession session = req.getSession();
            session.setAttribute(AUTH_ATT, driver.getId());
            resp.sendRedirect("/");
        } catch (AuthenticationException e) {
            req.setAttribute("errorMsg", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(req,resp);
        }
    }
}