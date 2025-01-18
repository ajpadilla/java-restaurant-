package restautant.kitchen.order.application;

import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.order.domain.OrderRepository;
import restautant.kitchen.order.domain.OrderSaga;
import restautant.kitchen.order.infrastructure.persistence.RedisAdapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderValidationService {
    private final RedisAdapter redisAdapter;

    private final OrderRepository orderRepository;

    public OrderValidationService(RedisAdapter redisAdapter, OrderRepository orderRepository) {
        this.redisAdapter = redisAdapter;
        this.orderRepository = orderRepository;
    }

    private boolean validateOrderCompletion(OrderSaga orderSaga, HashMap<String, Serializable> orderMap, Order order) {
        boolean isComplete = orderSaga.isOrderComplete();

        if (isComplete) {
            System.out.println("Order " + orderSaga.getOrderId() + " is complete.");
            // Actualizar en Redis y en la base de datos
            this.redisAdapter.saveSaga(orderMap.get("order_id").toString(), orderSaga);
            order.markAsReady();
            this.orderRepository.save(order);
        } else {
            System.out.println("Order " + orderSaga.getOrderId() + " is still incomplete.");
            // Guardar solo en Redis temporalmente
            this.redisAdapter.saveSaga(orderMap.get("order_id").toString(), orderSaga);
        }

        return isComplete;
    }

    private boolean validatePlateCompletion(OrderSaga orderSaga, String plateId) {
        boolean isPlateComplete = orderSaga.isPlateComplete(plateId);

        if (isPlateComplete) {
            System.out.println("Plate " + plateId + " in order " + orderSaga.getOrderId() + " is complete.");
        } else {
            System.out.println("Plate " + plateId + " in order " + orderSaga.getOrderId() + " is still incomplete.");
        }
        return isPlateComplete;
    }

    public void receivedIngredient(OrderSaga orderSaga, HashMap<String, Serializable> orderMap, Order order) {
        List<HashMap<String, Serializable>> plates = (List<HashMap<String, Serializable>>)  orderMap.get("plate");

        for (HashMap<String, Serializable> plate : plates) {
            String plateId = (String) plate.get("plate_id");
            List<HashMap<String, Serializable>> plateIngredients =
                    (List<HashMap<String, Serializable>>) plate.get("plate_ingredients");
            for (HashMap<String, Serializable> ingredientMap : plateIngredients) {
                ingredientMap.forEach((ingredientName, quantity) -> {
                    orderSaga.markIngredientReceived(plateId, ingredientName, Integer.parseInt(quantity.toString()));

                    if (this.validatePlateCompletion(orderSaga, plateId)) {
                        System.out.println("All ingredients for plate" + plateId +" are complete");
                    }

                });
            }

            if (this.validateOrderCompletion(orderSaga, orderMap, order)) {
                System.out.println("All plates in order" + orderMap.get("order_id") + " are complete");
            }
        }
    }
}
