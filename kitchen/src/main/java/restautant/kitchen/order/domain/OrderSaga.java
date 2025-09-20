package restautant.kitchen.order.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSaga implements Serializable {
    private final String orderId;
    private final Map<String, Map<String, Integer>> plateIngredientMap;

    @JsonCreator
    public OrderSaga(
            @JsonProperty("id") Map<String, String> idMap,
            @JsonProperty("plates") List<Map<String, Object>> plates
    ) {
        this.orderId = idMap != null ? idMap.get("value") : null;
        this.plateIngredientMap = new HashMap<>();

        if (plates != null) {
            plates.forEach(plate -> {
                Map<String, String> plateIdMap = (Map<String, String>) plate.get("id");
                String plateId = plateIdMap.get("value");

                List<Map<String, Object>> ingredients = (List<Map<String, Object>>) plate.get("ingredients");
                Map<String, Integer> ingredientMap = new HashMap<>();

                ingredients.forEach(ingredient -> {
                    Map<String, String> ingredientIdMap = (Map<String, String>) ingredient.get("name");
                    String ingredientName = ingredientIdMap.get("value");
                    Integer quantity = (Integer) ((Map<String, Object>) ingredient.get("quantity")).get("value");
                    ingredientMap.put(ingredientName, quantity);
                });

                this.plateIngredientMap.put(plateId, ingredientMap);
            });
        }
    }

    public OrderSaga(Order order) {
        this.orderId = order.getId().getValue();
        this.plateIngredientMap = new HashMap<>();

        // Inicializa la estructura con los platos y sus ingredients
        order.getPlates().forEach(plate -> {
            Map<String, Integer> ingredientMap = new HashMap<>();
            plate.getIngredients().forEach(ingredient -> {
                ingredientMap.put(ingredient.getName().getValue(), ingredient.getQuantity().getValue());
            });
            this.plateIngredientMap.put(plate.getId().getValue(), ingredientMap);
        });
    }

    public Map<String, Integer> getIngredientsForPlats(String plateId) {
        return this.plateIngredientMap.getOrDefault(plateId, new HashMap<>());
    }

    public void markIngredientReceived(String plateId, String ingredientName, int quantityReceived) {
        if (this.plateIngredientMap.containsKey(plateId)) {
            Map<String, Integer> ingredientMap = this.plateIngredientMap.get(plateId);
            ingredientMap.put(ingredientName, ingredientMap.getOrDefault(ingredientName, 0) - quantityReceived);
        }
    }

    public boolean isPlateComplete(String plateId) {
        return this.plateIngredientMap.getOrDefault(plateId, new HashMap<>())
                .values()
                .stream()
                .allMatch(quantity -> quantity <= 0);
    }

    public boolean isOrderComplete() {
        return this.plateIngredientMap.keySet()
                .stream()
                .allMatch(this::isPlateComplete);
    }

    public String getOrderId() {
        return orderId;
    }

    public Map<String, Map<String, Integer>> getPlateIngredientMap() {
        return plateIngredientMap;
    }
    @Override
    public String toString() {
        return "OrderSaga{" +
                "orderId='" + orderId + '\'' +
                ", plateIngredientMap=" + plateIngredientMap +
                '}';
    }
}
