package restautant.kitchen.ingredients.domain;

import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredient.domain.IngredientId;
import restautant.kitchen.ingredient.domain.IngredientName;
import restautant.kitchen.ingredient.domain.IngredientQuantity;

public final class IngredientMother {

    public static Ingredient create(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        return  new Ingredient(id, name, quantity);
    }

    public static Ingredient random() {
        return create(IngredientIdMother.random(), IngredientNameMother.random(), IngredientQuantityMother.random());
    }

}
