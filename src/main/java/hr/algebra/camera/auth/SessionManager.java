package hr.algebra.camera.auth;

import hr.algebra.camera.model.User;
import hr.algebra.camera.model.enums.UserRole;

public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    private SessionManager() { }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
