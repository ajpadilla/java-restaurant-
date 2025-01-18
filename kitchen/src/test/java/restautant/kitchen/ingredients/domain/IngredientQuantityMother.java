package restautant.kitchen.ingredients.domain;

import restaurant.order.shared.test.IntegerMother;

public class IngredientQuantityMother {

    public static IngredientQuantity create (Integer value) {
        return new IngredientQuantity(value);
    }

    public static IngredientQuantity random() {
        return create(IntegerMother.random());
    }

}
