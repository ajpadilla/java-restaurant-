package restaurant.order.menu.domain.exception;

public class TooManyIngredientsException extends RuntimeException {
    public TooManyIngredientsException(String message) {
        super(message);
    }
}