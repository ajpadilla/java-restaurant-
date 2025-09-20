package restautant.kitchen.order.domain;

import java.util.HashMap;
import java.util.Map;

public class OrderSagaService {
    private final Map<String, Integer> ingredientQuantities = new HashMap<>();
    private final Order orderData;

    public OrderSagaService(Order orderData) {
        this.orderData = orderData;
    }

}
