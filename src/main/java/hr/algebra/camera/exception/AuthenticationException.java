package hr.algebra.camera.exception;

public class AuthenticationException extends Exception  {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
