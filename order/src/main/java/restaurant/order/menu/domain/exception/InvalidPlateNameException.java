package restaurant.order.menu.domain.exception;


public class InvalidPlateNameException extends RuntimeException {
    public InvalidPlateNameException(String message) {
        super(message);
    }
}
