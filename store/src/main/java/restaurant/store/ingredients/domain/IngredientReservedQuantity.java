package restaurant.store.ingredients.domain;

import restaurant.store.shared.domain.IntValueObject;

public class IngredientReservedQuantity extends IntValueObject {
    public IngredientReservedQuantity(Integer value) {
        super(value);
    }

    public IngredientReservedQuantity() {
        super(null);
    }
}
