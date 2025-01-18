package restautant.kitchen.order.infrastructure;

import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.plate.domain.Plate;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public CreateOrderRequest toCreateOrderRequest(Order order) {

        // Convertir los platos del Order a PlateRequest
        List<PlateRequest> plateRequests = new ArrayList<>();

        for (Plate plate : order.getPlates()) {
            // Convertir los ingredientes del Plate a IngredientRequest
            List<IngredientRequest> ingredientRequests = new ArrayList<>();

            for (Ingredient ingredient : plate.getIngredients()) {
                IngredientRequest ingredientRequest = new IngredientRequest(
                        ingredient.getId().getValue(),
                        ingredient.getName().getValue(),
                        ingredient.getQuantity().getValue()
                );
                ingredientRequests.add(ingredientRequest);
            }

            // Crear PlateRequest
            PlateRequest plateRequest = new PlateRequest(
                    plate.getId().getValue(),
                    plate.getName().getValue(),
                    ingredientRequests
            );
            plateRequests.add(plateRequest);
        }

        // Crear y devolver CreateOrderRequest
        return new CreateOrderRequest(
                order.getId().getValue(),
                plateRequests
        );
    }
}
