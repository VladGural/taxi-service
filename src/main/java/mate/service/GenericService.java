package mate.service;

import java.util.List;
import mate.exception.DataException;

public interface GenericService<T> {
    T create(T element) throws DataException;

    T get(Long id) throws DataException;

    List<T> getAll();

    T update(T element) throws DataException;

    boolean delete(Long id);
}
