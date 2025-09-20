package restautant.kitchen.ingredient.domain;

import restautant.kitchen.shared.domain.IntValueObject;

public class IngredientQuantity extends IntValueObject {
    public IngredientQuantity(Integer value) {
        super(value);
    }

    public IngredientQuantity() {
        super(null);
    }
}
