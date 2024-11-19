package restaurant.store.ingredients.domain;

import restaurant.store.shared.domain.IntValueObject;

public class IngredientVersion extends IntValueObject {
    public IngredientVersion(Integer value) {
        super(value);
    }

    public IngredientVersion() {
        super(null);
    }
}
