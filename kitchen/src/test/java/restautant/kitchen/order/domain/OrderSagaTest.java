package restautant.kitchen.order.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import restautant.kitchen.helpers.StoreClientMock;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredients.domain.IngredientMother;
import restautant.kitchen.order.infrastructure.controller.CreateOrderRequest;
import restautant.kitchen.order.infrastructure.controller.OrderMapper;
import restautant.kitchen.plate.domain.*;
import restautant.kitchen.shared.infrastructure.feignclient.StoreClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderSagaTest {

    @Autowired
    private RedisPort redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private StoreClient storeClient; // Mock del cliente Feign
    @Test
    public void testOrderSagaInitialization() {

        Order order = OrderMother.random();

        OrderSaga orderSaga = new OrderSaga(order);

        Plate plate = order.plates.get(2);

        PlateId plateId = plate.getId();

        List<Ingredient> ingredientList = plate.getIngredients();

        Map<String, Integer> sagaIngredientsForPlats = orderSaga.getIngredientsForPlats(plateId.getValue());

        assertEquals(ingredientList.size(), sagaIngredientsForPlats.size());
    }

    @Test
    public void testGetIngredientsForNonExistentPlate() {
        Order order = OrderMother.random();
        OrderSaga orderSaga = new OrderSaga(order);

        Map<String, Integer> ingredients = orderSaga.getIngredientsForPlats("non-existent-plate");

        assertTrue(ingredients.isEmpty());
    }

    @Test
    public void testOrderWithMultiplePlates() {
        Order order = OrderMother.random();
        OrderSaga orderSaga = new OrderSaga(order);

        Plate plate0 = order.plates.get(0);
        PlateId plate0Id = plate0.getId();

        Plate plate1 = order.plates.get(0);
        PlateId plateId1 = plate1.getId();

        Map<String, Integer> plate1Ingredients = orderSaga.getIngredientsForPlats(plate0Id.getValue());
        Map<String, Integer> plate2Ingredients = orderSaga.getIngredientsForPlats(plateId1.getValue());

        assertEquals(plate0.getIngredients().size(), plate1Ingredients.size());
        assertEquals(plate1.getIngredients().size(), plate2Ingredients.size());

    }

    @Test
    public void testImmutableIngredientsMap() {

        Order order = OrderMother.random();
        OrderSaga orderSaga = new OrderSaga(order);

        // Act
        Map<String, Integer> ingredients = orderSaga.getIngredientsForPlats("plate-1");
        ingredients.put("new-ingredient", 10);

        // Assert: Verifica que el estado interno no cambió
        Map<String, Integer> originalIngredients = orderSaga.getIngredientsForPlats("plate-1");
        assertFalse(originalIngredients.containsKey("new-ingredient"));
    }

    @Test
    public void shouldValidateOrderSagaMethods() throws JsonProcessingException {

        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(IngredientMother.random());
        }

        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            plates.add(PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients));
        }

        Order order = OrderMother.create(OrderIdMother.random(), plates);
        OrderSaga orderSaga = new OrderSaga(order);
        String orderSagaToJson = objectMapper.writeValueAsString(orderSaga);
        this.redisService.saveSaga(order.getId().getValue(), orderSagaToJson);

        OrderMapper orderMapper = new OrderMapper();
        CreateOrderRequest createOrderRequest = orderMapper.toCreateOrderRequest(order);

        when(storeClient.save(any(CreateOrderRequest.class)))
                .thenReturn(StoreClientMock.saveResponse(order));

        // Ejecutar el método que estás probando
        ResponseEntity<HashMap<String, Serializable>> response = storeClient.save(createOrderRequest);


        // Recuperar OrderSaga desde Redis
        // Recuperar el JSON desde Redis
        String retrievedOrderSagaJson = (String) this.redisService.getSaga(order.getId().getValue());
        assertNotNull(retrievedOrderSagaJson);
        OrderSaga orderSagaRetrieved = objectMapper.readValue(retrievedOrderSagaJson, OrderSaga.class);

        // Validar que el OrderSaga recuperado es igual al original
        assertEquals(orderSaga.getOrderId(), orderSagaRetrieved.getOrderId());
        assertEquals(orderSaga.getPlateIngredientMap(), orderSagaRetrieved.getPlateIngredientMap());


        // Validar la respuesta
        assertNotNull(response.getBody());
        assertEquals(order.getId().getValue(), response.getBody().get("order_id"));

        // Validar los platos
        List<Map<String, Object>> returnedPlates = (List<Map<String, Object>>) response.getBody().get("plate");
        assertEquals(plates.size(), returnedPlates.size());

        for (int i = 0; i < plates.size(); i++) {
            Plate plate = plates.get(i);
            Map<String, Object> returnedPlate = returnedPlates.get(i);

            assertEquals(plate.getId().getValue(), returnedPlate.get("plate_id"));
            assertEquals(plate.getName().getValue(), returnedPlate.get("plate_name"));

            List<Map<String, Integer>> returnedIngredients =
                    (List<Map<String, Integer>>) returnedPlate.get("plate_ingredients");
            assertEquals(plate.getIngredients().size(), returnedIngredients.size());

            System.out.println("Total Ingredients:" + returnedIngredients.size());

            for (Ingredient ingredient : ingredients) {
                String ingredientName = ingredient.getName().getValue();
                int quantity = ingredient.getQuantity().getValue();

                // Simular recepción parcial de ingredientes
                int quantityReceived = Math.max(1, Math.min(quantity, quantity / 2)); // Garantiza recibir entre 1 y quantity
                orderSaga.markIngredientReceived(plate.getId().getValue(), ingredientName, quantityReceived);

                // Validar que la cantidad se actualizó correctamente
                Map<String, Integer> updatedIngredients = orderSaga.getIngredientsForPlats(plate.getId().getValue());
                int remainingQuantity = quantity - quantityReceived;
                assertEquals(remainingQuantity, updatedIngredients.get(ingredientName));

                // Completar la recepción de ingredientes
                quantityReceived = Math.min(remainingQuantity, quantity); // Asegura que no se reste más de lo que queda
                orderSaga.markIngredientReceived(plate.getId().getValue(), ingredientName, quantityReceived);

                // Validar que el ingrediente está completamente recibido
                updatedIngredients = orderSaga.getIngredientsForPlats(plate.getId().getValue());
                assertEquals(0, updatedIngredients.get(ingredientName).intValue());
            }

            // Validar si el plato está completo
            assertTrue(orderSaga.isPlateComplete(plate.getId().getValue()));
        }

        // Validar si la orden está completa
        assertTrue(orderSaga.isOrderComplete());

        this.redisService.deleteSaga(order.getId().getValue());
    }
}
