package restaurant.order.ingredients.domain;

import restaurant.order.shared.domain.IntValueObject;

public class IngredientQuantity extends IntValueObject {
    public IngredientQuantity(Integer value) {
        super(value);
    }

    public IngredientQuantity() {
        super(null);
    }
}
