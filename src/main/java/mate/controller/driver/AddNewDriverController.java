package mate.controller.driver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mate.exception.DataException;
import mate.lib.Injector;
import mate.model.Driver;
import mate.service.DriverService;

public class AddNewDriverController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate");
    private final DriverService driverService =
            (DriverService) injector.getInstance(DriverService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/driver/add_new_driver.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String licenseNumber = req.getParameter("license_number");
            if (!Driver.isLicenseNumberCorrect(licenseNumber)) {
                throw new DataException("Incorrect LicenseNumber format");
            }
            Driver driver = new Driver();
            driver.setName(req.getParameter("name"));
            driver.setLogin(req.getParameter("login"));
            driver.setPassword(req.getParameter("password"));
            driver.setLicenseNumber(licenseNumber);
            driverService.create(driver);
            resp.sendRedirect("/drivers");
        } catch (DataException e) {
            req.setAttribute("errorMsg", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/driver/add_new_driver.jsp")
                    .forward(req, resp);
        }
    }
}
