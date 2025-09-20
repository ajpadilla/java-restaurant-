package restautant.kitchen.helpers;

import org.springframework.http.ResponseEntity;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.plate.domain.Plate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreClientMock {
    public static ResponseEntity<HashMap<String, Serializable>> saveResponse(Order order) {
        HashMap<String, Serializable> mockResponse = new HashMap<>();
        mockResponse.put("order_id", order.getId().getValue());
        mockResponse.put("plate",  (Serializable) mapPlates(order.getPlates()));
        return ResponseEntity.ok(mockResponse);
    }

    private static List<HashMap<String, Object>> mapPlates(List<Plate> plates) {
        List<HashMap<String, Object>> plateList = new ArrayList<>();
        for (Plate plate : plates) {
            HashMap<String, Object> plateData = new HashMap<>();
            plateData.put("plate_id", plate.getId().getValue());
            plateData.put("plate_name", plate.getName().getValue());
            plateData.put("plate_ingredients", mapIngredients(plate.getIngredients()));
            plateList.add(plateData);
        }
        return plateList;
    }

    private static List<HashMap<String, Integer>> mapIngredients(List<Ingredient> ingredients) {
        List<HashMap<String, Integer>> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            HashMap<String, Integer> ingredientData = new HashMap<>();
            ingredientData.put(ingredient.getName().getValue(), ingredient.getQuantity().getValue());
            ingredientList.add(ingredientData);
        }
        return ingredientList;
    }
}
