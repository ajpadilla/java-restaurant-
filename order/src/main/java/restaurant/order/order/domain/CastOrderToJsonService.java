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

        // Agregar información general de la orden
        orderMap.put("order_id", order.getId().getValue());

        // Transformar cada Plate en un mapa y recolectar en una lista
        List<HashMap<String, Serializable>> platesList = order.getPlates().stream()
                .map(plate -> {
                    HashMap<String, Serializable> plateMap = new HashMap<>();
                    plateMap.put("plate_id", plate.getId().getValue());
                    plateMap.put("plate_name", plate.getName().getValue());

                    // Transformar los ingredientes del Plate en mapas
                    List<HashMap<String, Serializable>> ingredientsList = plate.getIngredients().stream()
                            .map(ingredient -> {
                                HashMap<String, Serializable> ingredientMap = new HashMap<>();
                                ingredientMap.put("id", ingredient.getId().getValue());
                                ingredientMap.put("name", ingredient.getName().getValue());
                                ingredientMap.put("quantity", ingredient.getQuantity().getValue());
                                return ingredientMap;
                            })
                            .collect(Collectors.toList());

                    plateMap.put("ingredients", (Serializable) ingredientsList);

                    return plateMap;
                })
                .collect(Collectors.toList());

        orderMap.put("plates", (Serializable) platesList);

        return orderMap;
    }

}
