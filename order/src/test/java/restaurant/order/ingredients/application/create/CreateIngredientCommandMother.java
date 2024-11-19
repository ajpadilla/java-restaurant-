package restaurant.order.ingredients.application.create;

import restaurant.order.ingredients.domain.*;

public class CreateIngredientCommandMother {

    public static CreateIngredientCommand create(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        return new CreateIngredientCommand(id.getValue(), name.getValue(), quantity.getValue());
    }

    public static CreateIngredientCommand random() {
        return create(IngredientIdMother.random(), IngredientNameMother.random(), IngredientQuantityMother.random());
    }
}


