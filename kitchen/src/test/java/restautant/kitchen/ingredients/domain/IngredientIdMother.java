package restautant.kitchen.ingredients.domain;

import restaurant.order.shared.test.UuidMother;

public class IngredientIdMother {

    public static IngredientId create(String value) {
        return new IngredientId(value);
    }

    public static IngredientId random() {
        return create(UuidMother.random());
    }


}
