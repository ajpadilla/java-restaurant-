package restaurant.order.order.domain;

import restaurant.order.plates.domain.Plate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class CastOrderToJsonService {


    private final Order order;

    public CastOrderToJsonService(Order order)
    {
        this.order = order;
    }

    public HashMap<String, Serializable> createMap() {
        HashMap<String, Serializable> orderMap = new HashMap<>();

        Plate plate = order.getPlate();

        orderMap.put("order_id", order.getId().getValue());
        orderMap.put("plate_id", plate.getId().getValue());
        orderMap.put("plate_name", plate.getName().getValue());

        List<HashMap<String, Serializable>> ingredientsList = plate.getIngredients().stream()
                .map(ingredient -> {
                    HashMap<String, Serializable> ingredientMap = new HashMap<>();
                    ingredientMap.put("id", ingredient.getId().getValue());
                    ingredientMap.put("name", ingredient.getName().getValue());
                    ingredientMap.put("quantity", ingredient.getQuantity().getValue());
                    return ingredientMap;
                })
                .collect(Collectors.toList());

        orderMap.put("ingredients", (Serializable) ingredientsList);

        return orderMap;
    }

}
