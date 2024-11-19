package restaurant.store.ingredients.domain;

import restaurant.store.shared.domain.IntValueObject;

public class IngredientQuantity extends IntValueObject {
    public IngredientQuantity(Integer value) {
        super(value);
    }

    public IngredientQuantity() {
        super(null);
    }
}
