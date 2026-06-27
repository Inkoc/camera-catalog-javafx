package hr.algebra.camera.auth;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    private PasswordHasher() { }

    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verify(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
