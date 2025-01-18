package restautant.kitchen.ingredients.domain;

import restautant.kitchen.ingredient.domain.IngredientQuantity;
import restautant.kitchen.shared.test.IntegerMother;

public class IngredientQuantityMother {
    public static IngredientQuantity create (Integer value) {
        return new IngredientQuantity(value);
    }

    public static IngredientQuantity random() {
        return create(IntegerMother.random());
    }
}
