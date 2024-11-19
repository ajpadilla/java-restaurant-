package restaurant.order.ingredients.application.find;

import org.springframework.data.domain.Page;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.shared.domain.bus.query.Response;

public class ListOfIngredientResponse implements Response {
    private final Page<Ingredient> ingredients;

    public ListOfIngredientResponse(Page<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Page<Ingredient> response() {
        return ingredients;
    }
}
