package restaurant.order.menu.domain;

import restaurant.order.shared.test.WordMother;

public class IngredientNameMother {

    public static IngredientName create(String value){
        return new IngredientName(value);
    }

    public static IngredientName random() {
        return create(WordMother.random());
    }

}