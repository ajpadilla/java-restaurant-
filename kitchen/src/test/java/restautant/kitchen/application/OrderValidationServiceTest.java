package restautant.kitchen.application;

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
import restautant.kitchen.ingredient.domain.IngredientRepository;
import restautant.kitchen.ingredients.domain.IngredientMother;
import restautant.kitchen.order.application.OrderValidationService;
import restautant.kitchen.order.domain.*;
import restautant.kitchen.order.infrastructure.controller.CreateOrderRequest;
import restautant.kitchen.order.infrastructure.controller.OrderMapper;
import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.plate.domain.PlateIdMother;
import restautant.kitchen.plate.domain.PlateMother;
import restautant.kitchen.plate.domain.PlateNameMother;
import restautant.kitchen.shared.infrastructure.feignclient.StoreClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderValidationServiceTest {

    @Autowired
    private RedisPort redisService;

    @Autowired
    private ObjectMapper objectMapper;

    /*@Autowired
    private StoreClient storeClient;*/

    @Autowired
    private OrderValidationService validationService;

    @Autowired
    protected OrderRepository jpaOrderRepository;

    @Autowired
    protected IngredientRepository jpaIngredientRepository;

    @Mock
    private StoreClient storeClient; // Mock del cliente Feign

    @Test
    void shouldValidateOrderSagaWithMockedStoreClient() throws JsonProcessingException {
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

        // Validar la respuesta
        assertNotNull(response.getBody());
        assertEquals(order.getId().getValue(), response.getBody().get("order_id"));
    }

    @Test
    public void testValidateOrder() throws JsonProcessingException {

        //Order order = OrderMother.random();

        // Crear una orden con múltiples platos
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            this.jpaIngredientRepository.save(ingredient);
            ingredients.add(ingredient);
        }

        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 3; i++) { // Supongamos que queremos 3 platos
            plates.add(PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients));
        }

        Order order = OrderMother.create(OrderIdMother.random(), plates);

        // Guardar la orden
        //this.jpaOrderRepository.save(order);

        OrderSaga orderSaga = new OrderSaga(order);
        String orderSagaToJson = objectMapper.writeValueAsString(orderSaga);
        this.redisService.saveSaga(order.getId().getValue(), orderSagaToJson);

        OrderMapper orderMapper = new OrderMapper();
        CreateOrderRequest createOrderRequest = orderMapper.toCreateOrderRequest(order);

        ResponseEntity<HashMap<String, Serializable>> response = this.storeClient.save(createOrderRequest);
        HashMap<String, Serializable> orderMap = response.getBody();

        System.out.println("Order Map: " + orderMap);

        // Recuperar el JSON desde Redis
        String orderSagaJson = (String) this.redisService.getSaga(order.getId().getValue());

        // Deserializar el JSON a un objeto OrderSaga
        OrderSaga orderSagaRetrieved = objectMapper.readValue(orderSagaJson, OrderSaga.class);

        System.out.println("OrderSaga Retrieved: " + orderSagaRetrieved);

        validationService.receivedIngredient(orderSagaRetrieved, orderMap, order);

        assertTrue(orderSagaRetrieved.isOrderComplete());

        this.redisService.deleteSaga(order.getId().getValue());
    }
}
