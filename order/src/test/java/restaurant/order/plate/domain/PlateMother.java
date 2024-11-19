package restaurant.order.plate.domain;

import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateName;

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
