package mate.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.dao.CarDao;
import mate.exception.DataProcessingException;
import mate.lib.Dao;
import mate.model.Car;
import mate.model.Driver;
import mate.model.Manufacturer;
import mate.util.ConnectionUtil;

@Dao
public class CarDaoImpl implements CarDao {
    @Override
    public Car create(Car car) {
        String insertCarRequest = "INSERT INTO cars (model, manufacturer_id) VALUES(?, ?);";
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement insertCarStatement =
                                 connection.prepareStatement(insertCarRequest,
                                         Statement.RETURN_GENERATED_KEYS)) {
            insertCarStatement.setString(1, car.getModel());
            insertCarStatement.setLong(2, car.getManufacturer().getId());
            insertCarStatement.executeUpdate();
            ResultSet resultSet = insertCarStatement.getGeneratedKeys();
            if (resultSet.next()) {
                car.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't insert car"
                    + car + "  to DB", e);
        }
        if (car.getDrivers() != null && car.getDrivers().size() > 0) {
            setDriversForCar(car);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        String getCarByIdRequest = "SELECT c.id AS car_id, model, "
                + "m.id AS manufacturer_id, m.name, m.country "
                + "FROM cars c JOIN manufacturers m "
                + "ON c.manufacturer_id = m.id "
                + "WHERE c.is_deleted = false AND c.id = ?;";
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement getCarByIdStatement = connection
                             .prepareStatement(getCarByIdRequest)) {
            getCarByIdStatement.setLong(1, id);
            ResultSet resultSet = getCarByIdStatement.executeQuery();
            if (resultSet.next()) {
                car = parseCarResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get car "
                    + car + "  from DB", e);
        }
        if (car != null) {
            car.setDrivers(getDriversForCar(id));
        }
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> getAll() {
        String getAllCarsRequest = "SELECT c.id AS car_id, model, "
                + "m.id AS manufacturer_id, m.name, m.country "
                + "FROM cars c JOIN manufacturers m "
                + "ON c.manufacturer_id = m.id "
                + "WHERE c.is_deleted = false;";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement getAllCarsStatement = connection
                             .prepareStatement(getAllCarsRequest)) {
            ResultSet resultSet = getAllCarsStatement.executeQuery();
            while (resultSet.next()) {
                cars.add(parseCarResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all cars from DB", e);
        }
        for (Car car : cars) {
            car.setDrivers(getDriversForCar(car.getId()));
        }
        return cars;
    }

    @Override
    public Car update(Car car) {
        Car oldCar = get(car.getId()).get();
        String updateCarRequest = "UPDATE cars SET model = ?, manufacturer_id = ? "
                + "WHERE is_deleted = false AND id  = ?;";
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement updateCarStatement =
                         connection.prepareStatement(updateCarRequest)) {
            updateCarStatement.setString(1, car.getModel());
            updateCarStatement.setLong(2, car.getManufacturer().getId());
            updateCarStatement.setLong(3, car.getId());
            updateCarStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update car by id "
                    + car + " from DB", e);
        }
        if (oldCar.getDrivers() != null && oldCar.getDrivers().size() > 0) {
            deleteDriversForCar(oldCar.getId());
        }
        if (car.getDrivers() != null && car.getDrivers().size() > 0) {
            setDriversForCar(car);
        }
        return oldCar;
    }

    @Override
    public boolean delete(Long id) {
        String deleteRequest = "UPDATE cars SET is_deleted = true WHERE id  = ?;";
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement deleteStatement =
                         connection.prepareStatement(deleteRequest)) {
            deleteStatement.setLong(1, id);
            return deleteStatement.executeUpdate() >= 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete car by id "
                    + id + " from DB", e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String getAllByDriverRequest = "SELECT car_id "
                + "FROM cars_drivers "
                + "JOIN cars ON cars_drivers.car_id = cars.id "
                + "WHERE cars.is_deleted = false AND driver_id = ?;";
        Set<Long> carIdSet = new HashSet<>();
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement getAllByDriverStatement =
                          connection.prepareStatement(getAllByDriverRequest)) {
            getAllByDriverStatement.setLong(1, driverId);
            ResultSet resultSet = getAllByDriverStatement.executeQuery();
            while (resultSet.next()) {
                carIdSet.add(resultSet.getObject("car_id", Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get car by driver id "
                    + driverId + " from DB", e);
        }
        List<Car> cars = new ArrayList<>();
        for (Long carId : carIdSet) {
            cars.add(get(carId).get());
        }
        return cars;
    }

    private Car parseCarResultSet(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject("manufacturer_id", Long.class));
        manufacturer.setName(resultSet.getString("m.name"));
        manufacturer.setCountry(resultSet.getString("m.country"));
        Car car = new Car();
        car.setId(resultSet.getObject("car_id", Long.class));
        car.setModel(resultSet.getString("model"));
        car.setManufacturer(manufacturer);
        return car;
    }

    private List<Driver> getDriversForCar(Long id) {
        String getDriversByIdRequest = "SELECT id, name, login, password, license_number "
                + "FROM drivers d JOIN cars_drivers cd "
                + "ON d.id = cd.driver_id "
                + "WHERE cd.car_id = ?;";
        List<Driver> driversList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement getDriversByIdStatement = connection
                         .prepareStatement(getDriversByIdRequest)) {
            getDriversByIdStatement.setLong(1, id);
            ResultSet resultSet = getDriversByIdStatement.executeQuery();
            while (resultSet.next()) {
                driversList.add(parseDriversResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get drivers "
                    + driversList + "  from DB", e);
        }
        return driversList;
    }

    private Driver parseDriversResultSet(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject("id", Long.class));
        driver.setName(resultSet.getString("name"));
        driver.setLogin(resultSet.getString("login"));
        driver.setPassword(resultSet.getString("password"));
        driver.setLicenseNumber(resultSet.getString("license_number"));
        return driver;
    }

    private void setDriversForCar(Car car) {
        String insertDriversForCarRequest = "INSERT "
                + "INTO cars_drivers(car_id, driver_id) VALUES(?, ?);";
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement insertDriversForCarStatement =
                         connection.prepareStatement(insertDriversForCarRequest)) {
            insertDriversForCarStatement.setLong(1, car.getId());
            for (Driver driver : car.getDrivers()) {
                insertDriversForCarStatement.setLong(2, driver.getId());
                insertDriversForCarStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't insert Drivers For Car to DB", e);
        }
    }

    private void deleteDriversForCar(Long carId) {
        String deleteDriversForCarRequest = "DELETE FROM cars_drivers WHERE car_id = ?;";
        try (Connection connection = ConnectionUtil.getConnect();
                 PreparedStatement deleteDriversForCarStatement =
                         connection.prepareStatement(deleteDriversForCarRequest)) {
            deleteDriversForCarStatement.setLong(1, carId);
            deleteDriversForCarStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete Drivers For Car to DB", e);
        }
    }
}
