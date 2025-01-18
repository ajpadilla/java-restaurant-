package restaurant.order.order.domain;

import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.plate.domain.PlateIdMother;
import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plate.domain.PlateNameMother;
import restaurant.order.plates.domain.Plate;

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
