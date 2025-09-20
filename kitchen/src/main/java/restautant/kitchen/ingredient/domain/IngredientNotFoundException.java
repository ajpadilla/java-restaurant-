package restautant.kitchen.ingredient.domain;

public final class IngredientNotFoundException extends RuntimeException{

    public IngredientNotFoundException(String message) {
        super(message);
    }

    public IngredientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
