package mate.service.impl;

import java.util.List;
import java.util.Optional;
import mate.dao.ManufacturerDao;
import mate.exception.IllegalArgumentException;
import mate.lib.Inject;
import mate.lib.Service;
import mate.model.Manufacturer;
import mate.service.ManufacturerService;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Inject
    private ManufacturerDao manufacturerDao;
    
    @Override
    public Manufacturer create(Manufacturer manufacturer) throws IllegalArgumentException {
        Optional<Manufacturer> optionalManufacturer =
                manufacturerDao.getManufacturerByName(manufacturer.getName());
        if (optionalManufacturer.isPresent()) {
            throw new IllegalArgumentException("Manufacturer with same Name already exist");
        }
        return manufacturerDao.create(manufacturer);
    }

    @Override
    public Manufacturer get(Long id) throws IllegalArgumentException {
        Optional<Manufacturer> optionalManufacturer = manufacturerDao.get(id);
        if (optionalManufacturer.isEmpty()) {
            throw new IllegalArgumentException("Don't exist Manufacturer dy id " + id);
        }
        return optionalManufacturer.get();
    }

    @Override
    public List<Manufacturer> getAll() {
        return manufacturerDao.getAll();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) throws IllegalArgumentException {

        Optional<Manufacturer> optionalManufacturer =
                manufacturerDao.getManufacturerByName(manufacturer.getName());
        if (optionalManufacturer.isPresent()
                && !optionalManufacturer.get().getId().equals(manufacturer.getId())) {
            throw new IllegalArgumentException("Manufacturer with same Name already exist");
        }
        return manufacturerDao.update(manufacturer);
    }

    @Override
    public boolean delete(Long id) {
        return manufacturerDao.delete(id);
    }
}
