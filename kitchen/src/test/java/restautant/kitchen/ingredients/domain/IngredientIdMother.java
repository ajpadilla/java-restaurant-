package restautant.kitchen.ingredients.domain;


import restautant.kitchen.ingredient.domain.IngredientId;
import restautant.kitchen.shared.test.UuidMother;

public class IngredientIdMother {

    public static IngredientId create(String value) {
        return new IngredientId(value);
    }

    public static IngredientId random() {
        return create(UuidMother.random());
    }


}
