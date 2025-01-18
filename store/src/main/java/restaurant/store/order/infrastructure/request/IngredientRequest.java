package restaurant.store.order.infrastructure.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {

    private String id;
    private String name;
    private int quantity; // Usa int o BigDecimal según el caso

    @Override
    public String toString() {
        return "IngredientRequest{" +
                "ingredientId='" + id + '\'' +
                ", ingredientName='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }

}
