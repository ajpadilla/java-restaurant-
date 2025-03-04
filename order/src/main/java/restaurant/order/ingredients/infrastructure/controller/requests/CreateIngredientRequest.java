package restaurant.order.ingredients.infrastructure.controller.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientRequest {
    private String id;
    private String name;
    private Integer quantity;
}
