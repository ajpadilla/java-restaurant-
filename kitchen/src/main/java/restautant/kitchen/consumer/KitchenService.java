package restautant.kitchen.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import restautant.kitchen.order.application.OrderValidationService;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.order.domain.OrderSaga;
import restautant.kitchen.order.domain.OrderService;
import restautant.kitchen.order.domain.RedisPort;
import restautant.kitchen.order.infrastructure.controller.CreateOrderRequest;
import restautant.kitchen.order.infrastructure.controller.OrderMapper;
import restautant.kitchen.shared.infrastructure.feignclient.StoreClient;

import java.io.Serializable;
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

    private final StoreClient storeClient;

    private final RedisPort redisService;

    @Autowired
    private OrderValidationService validationService;

    @Autowired
    public KitchenService(ObjectMapper objectMapper, OrderService orderService, StoreClient storeClient, RedisPort redisService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.storeClient = storeClient;
        this.redisService = redisService;
        this.sagaMap = new HashMap<>();
    }

    @KafkaListener(topics = "orderCreatedTopic", groupId = "myGroup")
    public void consume(String orderJson) {
        try {
            HashMap orderOjbect =  objectMapper.readValue(orderJson, HashMap.class);

            Order order = this.orderService.create(orderOjbect);

            OrderMapper orderMapper = new OrderMapper();
            CreateOrderRequest createOrderRequest = orderMapper.toCreateOrderRequest(order);

            String orderToJson = objectMapper.writeValueAsString(order);
            this.redisService.saveSaga(order.getId().getValue(), orderToJson);

            ResponseEntity<HashMap<String, Serializable>> response = this.storeClient.save(createOrderRequest);
            HashMap<String, Serializable> orderMap = response.getBody();

            // Procesar el orderMap
            System.out.println("Order received with ingredients: " + order.getId().getValue());
            System.out.println("CreateOrderRequest: " + createOrderRequest.toString());
            System.out.println("Order Map: " + orderMap);

            // Recuperar el JSON desde Redis
            String orderSagaJson = (String) this.redisService.getSaga(order.getId().getValue());

            System.out.println("orderSagaJson: " + orderSagaJson);

            // Deserializar el JSON a un objeto OrderSaga
            OrderSaga orderSagaRetrieved = objectMapper.readValue(orderSagaJson, OrderSaga.class);

            System.out.println("orderSagaRetrieved:" + orderSagaRetrieved.toString());

           this.validationService.receivedIngredient(orderSagaRetrieved, orderMap, order);

            if (orderSagaRetrieved.isOrderComplete()) {
                this.redisService.deleteSaga(order.getId().getValue());
                System.out.println("Sending message to order service to confirm order completed successfully");
            }
        } catch (Exception e) {
            System.err.println("Error sending to Store: " + e.getMessage());
            //retryOrMarkAsPending(orderJson); // Reintentar o marcar como pendiente
        }
    }

    private void retryOrMarkAsPending(String orderJson) {
        // Lógica para reintentar o almacenar en un estado pendiente
        try {
            kafkaTemplate.send("pendingOrdersTopic", orderJson);
            System.out.println("Order sent to pendingOrdersTopic for retry.");
        } catch (Exception e) {
            System.err.println("Failed to send to pendingOrdersTopic: " + e.getMessage());
            // Lógica adicional para notificar o registrar en base de datos como fallido
        }
    }

    @KafkaListener(topics = "pendingOrdersTopic", groupId = "pendingOrdersGroup")
    public void processPendingOrders(String orderJson) {
        try {
            System.out.println("Retrying order: " + orderJson);
            HashMap orderObject = objectMapper.readValue(orderJson, HashMap.class);

            Order order = orderService.create(orderObject);

            // Mapear a CreateOrderRequest
            OrderMapper orderMapper = new OrderMapper();
            CreateOrderRequest createOrderRequest = orderMapper.toCreateOrderRequest(order);

            // Intentar enviar nuevamente a Store
            storeClient.save(createOrderRequest);
            System.out.println("Order successfully processed after retry: " + orderJson);
        } catch (Exception e) {
            System.err.println("Failed to process order on retry: " + e.getMessage());
            //markAsFailed(orderJson, e.getMessage()); // Opcional: almacenar en DB como fallido
        }
    }

    /*private void markAsFailed(String orderJson, String reason) {
        // Guardar en una tabla de base de datos (ejemplo: `failed_orders`)
        //FailedOrder failedOrder = new FailedOrder(orderJson, reason, LocalDateTime.now());
        failedOrderRepository.save(failedOrder);
        System.out.println("Order marked as failed in database: " + orderJson);
    }

    @KafkaListener(topics = "failedOrdersTopic", groupId = "failedOrdersGroup")
    public void retryFailedOrder(String failedOrderJson) {
        try {
            FailedOrderEvent failedOrder = objectMapper.readValue(failedOrderJson, FailedOrderEvent.class);
            // Intentar procesar el pedido nuevamente
            retryOrNotify(failedOrder);
        } catch (Exception e) {
            System.err.println("Failed to process failed order: " + e.getMessage());
            // Opcional: Alerta de fallo persistente
        }
    }

    private void retryOrNotify(FailedOrderEvent failedOrder) {
        // Lógica para reintentar el pedido o notificar al equipo
    }*/



    @KafkaListener(topics = "ingredientReservedTopic", groupId = "kitchenGroup")
    public void handleIngredientReserved(String reservedJson) throws JsonProcessingException {
        /*IngredientReservedEvent event = objectMapper.readValue(reservedJson, IngredientReservedEvent.class);

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
        }*/
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
