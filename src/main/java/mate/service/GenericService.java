package mate.service;

import java.util.List;
import mate.exception.IllegalArgumentException;

public interface GenericService<T> {
    T create(T element) throws IllegalArgumentException;

    T get(Long id) throws IllegalArgumentException;

    List<T> getAll();

    T update(T element) throws IllegalArgumentException;

    boolean delete(Long id);
}
