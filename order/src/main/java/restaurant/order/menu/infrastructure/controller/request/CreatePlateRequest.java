package restaurant.order.menu.infrastructure.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlateRequest {
    private String id;
    private String name;
    private List<String> ingredientsIds;
}
