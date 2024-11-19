package restautant.kitchen.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import restautant.kitchen.consumer.events.IngredientAvailabilityRequest;
import restautant.kitchen.consumer.events.IngredientReservedEvent;
import restautant.kitchen.consumer.events.OrderCompletedEvent;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.order.domain.OrderSaga;
import restautant.kitchen.order.domain.OrderService;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KitchenService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    private final Map<String, OrderSaga> sagaMap;

    @Autowired
    public KitchenService(ObjectMapper objectMapper, OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.sagaMap = new HashMap<>();
    }

    @KafkaListener(topics = "orderCreatedTopic", groupId = "myGroup")
    public void consume(String orderJson) {
        try {
            HashMap orderOjbect =  objectMapper.readValue(orderJson, HashMap.class);

            Order order = this.orderService.create(orderOjbect);

            // Iniciar la Saga para el pedido
            OrderSaga saga = new OrderSaga(order);
            sagaMap.put(order.getId().getValue(), saga);

            // Enviar evento de solicitud de disponibilidad y reserva temporal de ingredientes
            order.getPlate().getIngredients().forEach(ingredient -> {
                IngredientAvailabilityRequest request = new IngredientAvailabilityRequest(order.getId().getValue(), ingredient.getName(), ingredient.getQuantity().getValue());
                try {
                    kafkaTemplate.send("ingredientAvailabilityRequestTopic", objectMapper.writeValueAsString(request));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            // Imprimir los ingredientes
            System.out.println("Order received with ingredients: " + order.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "ingredientReservedTopic", groupId = "kitchenGroup")
    public void handleIngredientReserved(String reservedJson) throws JsonProcessingException {
        IngredientReservedEvent event = objectMapper.readValue(reservedJson, IngredientReservedEvent.class);

        String orderId = event.getOrderId();
        Ingredient ingredient = event.getIngredient();
        int reservedQuantity = event.getReservedQuantity();

        OrderSaga saga = sagaMap.get(orderId);
        if (saga != null) {
            saga.updateIngredient(ingredient, reservedQuantity);

            if (saga.isComplete()) {
                prepareDish(saga.getOrder());

                // Confirmar la reserva de los ingredientes
                OrderCompletedEvent orderCompletedEvent = new OrderCompletedEvent(saga.getOrder().getId().getValue());
                kafkaTemplate.send("orderCompletedTopic",  objectMapper.writeValueAsString(orderCompletedEvent));

                // Eliminar el pedido de la Saga
                sagaMap.remove(orderId);
            } else {
                log.info("Order {} is still waiting for more ingredients", orderId);
            }
        }
    }

   /* @KafkaListener(topics = "orderFailedTopic", groupId = "kitchenGroup")
    public void handleOrderFailed(String failedJson) {
        OrderFailedEvent event = objectMapper.readValue(failedJson, OrderFailedEvent.class);
        String orderId = event.getOrderId();

        OrderSaga saga = sagaMap.get(orderId);
        if (saga != null) {
            // Cancelar la reserva de los ingredientes
            kafkaTemplate.send("orderExpiredTopic", createOrderExpiredEvent(saga.getOrder()));

            // Eliminar el pedido de la Saga
            sagaMap.remove(orderId);
        }
    }*/

    private void prepareDish(Order order) {
        log.info("Preparing dish for order {}", order.getId().getValue());
        // Lógica para preparar el plato
    }
}
