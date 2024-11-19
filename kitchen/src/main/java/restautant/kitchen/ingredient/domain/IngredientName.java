package restautant.kitchen.ingredient.domain;


import restautant.kitchen.shared.domain.StringValueObject;

public class IngredientName extends StringValueObject {
    public IngredientName(String value) {
        super(value);
    }

    public IngredientName() {
        super("");
    }

}
