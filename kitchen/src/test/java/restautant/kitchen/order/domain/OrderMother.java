package restautant.kitchen.order.domain;


import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredients.domain.IngredientMother;
import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.plate.domain.PlateIdMother;
import restautant.kitchen.plate.domain.PlateMother;
import restautant.kitchen.plate.domain.PlateNameMother;

import java.util.ArrayList;
import java.util.List;

public class OrderMother {

    public static Order create(OrderId id, List<Plate> plate) {
        return  new Order(id, plate);
    }

    public static Order random() {

        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(IngredientMother.random());
        }

        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 3; i++) { // Supongamos que queremos 3 platos
            plates.add(PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients));
        }
        return create(OrderIdMother.random(), plates);
    }

}
