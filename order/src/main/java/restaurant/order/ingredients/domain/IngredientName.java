package restaurant.order.ingredients.domain;

import restaurant.order.shared.domain.StringValueObject;

public class IngredientName extends StringValueObject {
    public IngredientName(String value) {
        super(value);
    }

    public IngredientName() {
        super("");
    }

}
