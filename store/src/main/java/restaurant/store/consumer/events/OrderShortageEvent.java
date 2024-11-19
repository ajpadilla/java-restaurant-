package restaurant.store.consumer.events;

import lombok.*;
import restaurant.store.ingredients.domain.IngredientName;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderShortageEvent {
    private String orderId;
    private IngredientName ingredientName;
    private int reservedQuantity;
}
