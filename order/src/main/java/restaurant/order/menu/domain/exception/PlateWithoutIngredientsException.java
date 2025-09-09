package restaurant.order.menu.domain.exception;

public class PlateWithoutIngredientsException extends RuntimeException {
    public PlateWithoutIngredientsException(String message) {
        super(message);
    }
}
