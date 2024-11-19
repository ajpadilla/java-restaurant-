package restaurant.store.consumer.events;

import lombok.*;
import restaurant.store.ingredients.domain.IngredientName;

import java.time.Instant;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class IngredientReservation {
    private IngredientName ingredientName;
    private int reservedQuantity;
    private Instant reservationTime;
}
