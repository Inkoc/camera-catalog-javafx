package hr.algebra.camera.service;

import hr.algebra.camera.auth.PasswordHasher;
import hr.algebra.camera.auth.SessionManager;
import hr.algebra.camera.exception.AuthenticationException;
import hr.algebra.camera.model.User;
import hr.algebra.camera.model.enums.UserRole;
import hr.algebra.camera.repository.interfaces.IUserRepository;
import hr.algebra.camera.service.interfaces.IAuthService;

import java.util.Optional;

public class AuthService implements IAuthService {

    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String rawPassword) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty() || !PasswordHasher.verify(rawPassword, optionalUser.get().getHashedPassword())) {
            throw new AuthenticationException("Invalid username or password.");
        }

        User user = optionalUser.get();
        SessionManager.getInstance().login(user);

        return user;
    }

    @Override
    public void register(String username, String rawPassword) throws AuthenticationException {
        if (username == null || username.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new AuthenticationException("Username and password cannot be empty.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new AuthenticationException("Username already exists.");
        }

        String hashedPassword = PasswordHasher.hash(rawPassword);

        User newUser = new User(0, username, hashedPassword, UserRole.USER);

        userRepository.save(newUser);
    }
}
