package hr.algebra.camera.service.interfaces;

import hr.algebra.camera.exception.AuthenticationException;
import hr.algebra.camera.model.User;

public interface IAuthService {
    User login(String username, String rawPassword) throws AuthenticationException;
    void register(String username, String rawPassword) throws AuthenticationException;
}
