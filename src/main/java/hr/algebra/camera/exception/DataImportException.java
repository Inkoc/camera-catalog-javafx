package hr.algebra.camera.exception;

public class DataImportException extends RuntimeException {
    public DataImportException(String message) {
        super(message);
    }

    public DataImportException(String message, Throwable cause) {
        super(message, cause);
    }
}