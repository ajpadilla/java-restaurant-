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

    public Order create(HashMap orderObject) {

        // Convertir la lista de platos en objetos Plate
        List<HashMap<String, Object>> platesMap = (List<HashMap<String, Object>>) orderObject.get("plates");
        List<Plate> plates = new ArrayList<>();

        for (HashMap<String, Object> plateMap : platesMap) {
            // Convertir los ingredientes del plato
            List<HashMap<String, Object>> ingredientsMap = (List<HashMap<String, Object>>) plateMap.get("ingredients");
            List<Ingredient> ingredients = new ArrayList<>();

            for (HashMap<String, Object> ingredientMap : ingredientsMap) {
                Ingredient ingredient = new Ingredient(
                        new IngredientId(ingredientMap.get("id").toString()),
                        new IngredientName(ingredientMap.get("name").toString()),
                        new IngredientQuantity(Integer.parseInt(ingredientMap.get("quantity").toString()))
                );
                ingredients.add(ingredient);
            }

            // Crear el objeto Plate
            Plate plate = new Plate(
                    new PlateId(plateMap.get("plate_id").toString()),
                    new PlateName(plateMap.get("plate_name").toString()),
                    ingredients
            );
            plates.add(plate);
        }

        // Crear el objeto Order
        Order order = Order.create(
                new OrderId(orderObject.get("order_id").toString()),
                plates
        );

        // Publicar eventos de dominio si es necesario
        // this.bus.publish(order.pullDomainEvents());

        return order;
    }
}
