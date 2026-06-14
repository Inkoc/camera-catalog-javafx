package hr.algebra.camera.model;

import hr.algebra.camera.model.enums.UserRole;

public class User extends AbstractEntity{
    private String hashedPassword;
    private UserRole role;

    public User(int id, String name, String hashedPassword, UserRole role) {
        super(id, name);
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username=" + getName() +
                "role=" + role +
                '}';
    }
}
