package restaurant.store.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.store.consumer.events.*;
import restaurant.store.ingredients.domain.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StoreOrderConsumer {


    @Autowired
    private IngredientRepository ingredientRepository;

    private final ObjectMapper objectMapper;

    // Mapa para almacenar las reservas temporales
    private final Map<String, List<IngredientReservation>> reservationMap = new HashMap<>();


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public StoreOrderConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "ingredientAvailabilityRequestTopic", groupId = "warehouseGroup")
    public void handleAvailabilityRequest(String requestJson) throws JsonProcessingException {
        IngredientAvailabilityRequest request = objectMapper.readValue(requestJson, IngredientAvailabilityRequest.class);

        String orderId = request.getOrderId();
        IngredientName ingredientName = request.getIngredientName();
        int requestedQuantity = request.getQuantity();

        // Verificar y reservar temporalmente el ingrediente
        int availableQuantity = checkIngredientAvailability(ingredientName);

        if (availableQuantity >= requestedQuantity) {
            // Reservar temporalmente los ingredientes
            this.reserveIngredient(orderId, ingredientName, request.getQuantity());

            // Enviar evento de reserva
            IngredientReservedEvent reservedEvent = new IngredientReservedEvent(request.getOrderId(), ingredientName, requestedQuantity);
            kafkaTemplate.send("ingredientReservedTopic", objectMapper.writeValueAsString(reservedEvent));
        } else {
            // No hay suficientes ingredientes disponibles
            OrderShortageEvent shortageEvent = new OrderShortageEvent(request.getOrderId(), ingredientName, requestedQuantity);
            kafkaTemplate.send("ingredientShortageTopic", objectMapper.writeValueAsString(shortageEvent));
        }
    }

    @KafkaListener(topics = "orderCompletedTopic", groupId = "warehouseGroup")
    public void handleOrderCompleted(String completedJson) throws JsonProcessingException {
        OrderCompletedEvent event = objectMapper.readValue(completedJson, OrderCompletedEvent.class);

        // Obtener la lista de reservas para el pedido
        List<IngredientReservation> reservations = this.reservationMap.get(event.getOrderId());
        if (reservations != null && !reservations.isEmpty()) {
            // Confirmar y descontar cada reserva
            for (IngredientReservation reservation : reservations) {
                this.confirmReservation(reservation);
            }
            // Eliminar las reservas confirmadas del mapa
            reservationMap.remove(event.getOrderId());
        }
    }

    @KafkaListener(topics = "orderExpiredTopic", groupId = "warehouseGroup")
    public void handleOrderExpired(String expiredJson) throws JsonProcessingException {
        OrderExpiredEvent event = objectMapper.readValue(expiredJson, OrderExpiredEvent.class);

        // Obtener la lista de reservas para el pedido
        List<IngredientReservation> reservations = this.reservationMap.get(event.getOrderId());
        if (reservations != null && !reservations.isEmpty()) {
            // Confirmar y descontar cada reserva
            for (IngredientReservation reservation : reservations) {
                this.cancelReservation(reservation);
            }
            // Eliminar las reservas confirmadas del mapa
            reservationMap.remove(event.getOrderId());
        }
    }

    // Método para reservar ingredientes
    @Transactional
    public void reserveIngredient(String orderId, IngredientName name, int quantityToReserve) {
        Ingredient foundIngredient = ingredientRepository.searchByNameWithLock(name.getValue())
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found"));

        int availableQuantity = foundIngredient.getQuantity().getValue() - foundIngredient.getReservedQuantity().getValue();

        if (availableQuantity >= quantityToReserve) {
            foundIngredient.setReservedQuantity(
                   new IngredientReservedQuantity(foundIngredient.getReservedQuantity().getValue() + quantityToReserve)
            );
            this.ingredientRepository.save(foundIngredient);

            // Agregar la reserva temporal al mapa
            IngredientReservation reservation = new IngredientReservation(name, quantityToReserve, Instant.now());
            // Agregar la reserva a la lista de reservas del pedido
            reservationMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(reservation);
        }  else {
            throw new IllegalStateException("Not enough reserved ingredients to confirm.");
        }
    }

    // Método para confirmar una reserva (descontar ingredientes)

    @Transactional
    public void confirmReservation(IngredientReservation reservation) {
        IngredientName name = reservation.getIngredientName();
        int quantityToConfirm = reservation.getReservedQuantity();

        // Buscar el ingrediente en la base de datos con lock
        Ingredient foundIngredient = ingredientRepository.searchByNameWithLock(name.getValue())
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found"));

        if (foundIngredient.getReservedQuantity().getValue() >= quantityToConfirm) {
            // Descontar la cantidad confirmada permanentemente del inventario
            foundIngredient.setQuantity(new IngredientQuantity(
                    foundIngredient.getQuantity().getValue() - quantityToConfirm)
            );
            foundIngredient.setReservedQuantity(new IngredientReservedQuantity(
                    foundIngredient.getReservedQuantity().getValue() - quantityToConfirm)
            );
            ingredientRepository.save(foundIngredient);
        } else {
            throw new IllegalStateException("Not enough reserved ingredients to confirm.");
        }
    }
    // Método para cancelar una reserva (liberar ingredientes)
    @Transactional
    public void cancelReservation(IngredientReservation reservation) {
        IngredientName name = reservation.getIngredientName();
        int quantityToCancel = reservation.getReservedQuantity();

        // Buscar el ingrediente en la base de datos con lock
        Ingredient foundIngredient = ingredientRepository.searchByNameWithLock(name.getValue())
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found"));

        if (foundIngredient.getReservedQuantity().getValue() >= quantityToCancel) {
            // Liberar la cantidad reservada en el inventario
            foundIngredient.setReservedQuantity(new IngredientReservedQuantity(
                    foundIngredient.getReservedQuantity().getValue() - quantityToCancel)
            );
            ingredientRepository.save(foundIngredient);
        } else {
            throw new IllegalStateException("Not enough reserved ingredients to cancel.");
        }
    }

    private int checkIngredientAvailability(IngredientName ingredientName) {
       Ingredient foundIngredient = this.ingredientRepository.searchByName(ingredientName.getValue())
               .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found"));
       return foundIngredient.getQuantity().getValue();
    }
}
