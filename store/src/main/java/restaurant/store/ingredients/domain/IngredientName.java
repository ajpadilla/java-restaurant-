package restaurant.store.ingredients.domain;

import restaurant.store.shared.domain.StringValueObject;

public class IngredientName extends StringValueObject {
    public IngredientName(String value) {
        super(value);
    }

    public IngredientName() {
        super("");
    }

}
