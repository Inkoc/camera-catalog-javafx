package hr.algebra.camera.repository;

import hr.algebra.camera.model.User;

import java.util.Optional;

public interface IUserRepository extends IRepository<User>{
    Optional<User> findByUsername(String username);
}
