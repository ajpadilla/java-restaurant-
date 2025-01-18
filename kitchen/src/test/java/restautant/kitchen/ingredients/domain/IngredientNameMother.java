package restautant.kitchen.ingredients.domain;

import restautant.kitchen.ingredient.domain.IngredientName;
import restautant.kitchen.shared.test.WordMother;

public class IngredientNameMother {

    public static IngredientName create(String value){
        return new IngredientName(value);
    }

    public static IngredientName random() {
        return create(WordMother.random());
    }

}
