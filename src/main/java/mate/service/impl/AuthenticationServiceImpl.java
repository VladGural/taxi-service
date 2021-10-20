package mate.service.impl;

import java.util.Optional;
import mate.exception.AuthenticationException;
import mate.lib.Inject;
import mate.lib.Service;
import mate.model.Driver;
import mate.service.AuthenticationService;
import mate.service.DriverService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    @Inject
    private DriverService driverService;

    @Override
    public Driver login(String login, String password) throws AuthenticationException {
        Optional<Driver> optionalDriver = driverService.findByLogin(login);
        if (!optionalDriver.isPresent() || !optionalDriver.get().getPassword().equals(password)) {
            logger.info("User with login {} hasn't access Authentication", login);
            throw new AuthenticationException("Username or Password was incorrect");
        }
        logger.info("User with login {} has access Authentication", login);
        return optionalDriver.get();
    }
}
