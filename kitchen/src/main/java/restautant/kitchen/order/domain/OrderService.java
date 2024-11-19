package restautant.kitchen.order.domain;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredient.domain.IngredientId;
import restautant.kitchen.ingredient.domain.IngredientName;
import restautant.kitchen.ingredient.domain.IngredientQuantity;
import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.plate.domain.PlateId;
import restautant.kitchen.plate.domain.PlateName;
import restautant.kitchen.shared.domain.bus.event.EventBus;

@Service
public class OrderService {

    private final EventBus bus;

    public OrderService(@Qualifier("springApplicationEventBus") EventBus bus) {
        this.bus = bus;
    }

    public Order create(HashMap orderOjbect) {

        // Convertir la lista de ingredientes a una lista de objetos Ingredient
        List<HashMap<String, Object>> ingredientMaps = (List<HashMap<String, Object>>) orderOjbect.get("ingredients");
        List<Ingredient> ingredients = new ArrayList<>();

        for (HashMap<String, Object> ingredientMap : ingredientMaps) {
            Ingredient ingredient = new Ingredient(
                    new IngredientId(ingredientMap.get("id").toString()),
                    new IngredientName(ingredientMap.get("name").toString()),
                    new IngredientQuantity((Integer) ingredientMap.get("quantity"))
            );
            ingredients.add(ingredient);
        }

        Plate plate = new Plate(
                new PlateId(orderOjbect.get("plate_id").toString()),
                new PlateName(orderOjbect.get("plate_name").toString()),
                ingredients
        );

        Order order = Order.create(
                new OrderId(orderOjbect.get("order_id").toString()),
                plate
        );

     //   this.bus.publish(order.pullDomainEvents());

        return order;
    }


}
