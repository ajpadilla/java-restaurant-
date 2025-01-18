package restaurant.store.order.infrastructure.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlateRequest {
    private String plateId;
    private String plateName;
    private List<IngredientRequest> ingredients;

    @Override
    public String toString() {
        return "PlateRequest{" +
                "plateId='" + plateId + '\'' +
                ", plateName='" + plateName + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
