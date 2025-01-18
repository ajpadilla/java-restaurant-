package restautant.kitchen.ingredients.domain;

public final class IngredientMother {

    public static Ingredient create(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        return  new Ingredient(id, name, quantity);
    }

    public static Ingredient random() {
        return create(IngredientIdMother.random(), IngredientNameMother.random(), IngredientQuantityMother.random());
    }

}
