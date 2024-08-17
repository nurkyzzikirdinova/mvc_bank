package zikirdinova.mvc.exception;

public class DuplicateCodeException extends RuntimeException {
    public DuplicateCodeException(String message) {
        super(message);
    }

    public DuplicateCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
