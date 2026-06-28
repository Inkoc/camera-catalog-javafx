package hr.algebra.camera.repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    List<T> findAll();
    Optional<T> findById(int id);
    int save(T entity);
    void update(T entity);
    void deleteById(int id);
}
