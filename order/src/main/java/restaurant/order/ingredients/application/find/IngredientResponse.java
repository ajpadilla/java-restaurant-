package restaurant.order.ingredients.application.find;

import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.shared.domain.bus.query.Response;

public class IngredientResponse implements Response {

    private final Ingredient ingredient;

    public IngredientResponse(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }
}
