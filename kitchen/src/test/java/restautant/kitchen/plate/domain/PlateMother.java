package restautant.kitchen.plate.domain;

import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredients.domain.IngredientMother;

import java.util.ArrayList;
import java.util.List;

public class PlateMother {

    public static Plate create(PlateId id, PlateName name, List<Ingredient> ingredients) {
        return new Plate(id, name, ingredients);
    }

    public static Plate random() {
        return create(PlateIdMother.random(), PlateNameMother.random(), makeIngredientList());
    }

    public static List<Ingredient> makeIngredientList() {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < 5; i++) {
            ingredients.add(IngredientMother.random());
        }

        return ingredients;
    }

}
