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
public class CreateOrderRequest {

    private String orderId;
    private List<PlateRequest> plates;

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "orderId='" + orderId + '\'' +
                ", plates=" + plates +
                '}';
    }

}
