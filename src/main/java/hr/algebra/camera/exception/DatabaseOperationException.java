package hr.algebra.camera.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }
    public DatabaseOperationException(String message, Throwable e) {
        super(message, e);
    }
}
