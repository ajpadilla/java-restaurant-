package restautant.kitchen.consumer.events;

import jakarta.persistence.Entity;
import lombok.*;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredient.domain.IngredientName;

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
