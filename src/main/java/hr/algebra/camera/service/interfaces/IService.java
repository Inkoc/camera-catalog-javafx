package hr.algebra.camera.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
        List<T> findAll();
        Optional<T> findById(int id);
        int save(T entity);
        void update(T entity);
        void deleteById(int id);
}
