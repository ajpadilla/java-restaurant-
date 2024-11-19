package restaurant.store.consumer.events;

import lombok.*;
import restaurant.store.ingredients.domain.Ingredient;
import restaurant.store.ingredients.domain.IngredientName;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class IngredientAvailabilityRequest {
    private String orderId;
    private IngredientName ingredientName;
    private int quantity;
}
