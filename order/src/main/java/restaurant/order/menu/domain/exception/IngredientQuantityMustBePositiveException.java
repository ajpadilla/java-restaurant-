package restaurant.order.menu.domain.exception;

public class IngredientQuantityMustBePositiveException extends RuntimeException{
    public IngredientQuantityMustBePositiveException(String message) {
        super(message);
    }
}
