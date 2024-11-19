package restaurant.store.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import restaurant.store.consumer.events.IngredientAvailabilityRequest;
import restaurant.store.consumer.events.OrderShortageEvent;
import restaurant.store.ingredients.domain.IngredientQuantity;
import restaurant.store.ingredients.domain.IngredientRepository;
import restaurant.store.ingredients.infrastructure.api.IngredientPurchaseService;

@Service
public class PurchaseOrderConsumer {

    private final ObjectMapper objectMapper;

    @Autowired
    private IngredientRepository ingredientRepository;

    private KafkaTemplate<String, String> kafkaTemplate;

    private final IngredientPurchaseService purchaseService;

    public PurchaseOrderConsumer(ObjectMapper objectMapper, IngredientPurchaseService purchaseService) {
        this.objectMapper = objectMapper;
        this.purchaseService = purchaseService;
    }

    @KafkaListener(topics = "ingredientShortageTopic", groupId = "warehouseGroup")
    public void handleIngredientShortageEvent(String message) {
        try {
            // Convertir el mensaje JSON en un objeto OrderShortageEvent
            OrderShortageEvent shortageEvent = objectMapper.readValue(message, OrderShortageEvent.class);

            System.out.println("Received shortage event: " +
                    "Order ID: " + shortageEvent.getOrderId() +
                    ", Ingredient: " + shortageEvent.getIngredientName() +
                    ", Requested Quantity: " + shortageEvent.getReservedQuantity());

            int quantity = purchaseService.requestIngredientPurchase(
                    shortageEvent.getIngredientName().getValue(),
                    shortageEvent.getReservedQuantity()
            );

            this.ingredientRepository.updateByName(shortageEvent.getIngredientName().getValue(), new IngredientQuantity(quantity));

            // Usar el servicio para simular la compra de ingredientes

            IngredientAvailabilityRequest request = new IngredientAvailabilityRequest(shortageEvent.getOrderId(), shortageEvent.getIngredientName(), shortageEvent.getReservedQuantity());
            this.kafkaTemplate.send("ingredientAvailabilityRequestTopic", objectMapper.writeValueAsString(request));

        } catch (Exception e) {
            System.err.println("Failed to process ingredient shortage event: " + e.getMessage());
        }
    }

}
