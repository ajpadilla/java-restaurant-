package restaurant.order.shared.domain;

public final class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String message) {
        super(message);
    }

    public InvalidUUIDException(String message, Throwable cause) {
        super(message, cause);
    }
}
