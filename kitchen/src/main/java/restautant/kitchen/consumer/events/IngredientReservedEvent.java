package restautant.kitchen.consumer.events;

import lombok.*;
import restautant.kitchen.ingredient.domain.Ingredient;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class IngredientReservedEvent {
    private String orderId;
    private Ingredient ingredient;
    private int reservedQuantity;
}
