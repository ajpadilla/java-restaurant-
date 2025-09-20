package restautant.kitchen.order.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import restautant.kitchen.shared.infrastructure.feignclient.StoreClient;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisPort redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreClient storeClient;

    @Test
    public void testSaveOrderSagaInitialization() throws JsonProcessingException {
        Order order = OrderMother.random();
        OrderSaga orderSaga = new OrderSaga(order);
        String orderSagaToJson = objectMapper.writeValueAsString(orderSaga);

        String orderId = order.getId().getValue();
        this.redisService.saveSaga(order.getId().getValue(), orderSagaToJson);

        Serializable retrievedSaga = redisService.getSaga(orderId);
        assertNotNull(retrievedSaga,"La saga no debería ser nula");
        assertEquals(orderSagaToJson, retrievedSaga,"El contenido de la saga recuperada debería coincidir");
        this.redisService.deleteSaga(orderId);
    }

    @Test
    public void testDeleteSaga() throws InterruptedException, JsonProcessingException{
        // Preparar datos
        Order order = OrderMother.random();
        OrderSaga orderSaga = new OrderSaga(order);
        String orderSagaToJson = objectMapper.writeValueAsString(orderSaga);

        // Guardar en Redis
        String orderId = order.getId().getValue();
        redisService.saveSaga(orderId, orderSagaToJson);

        // Validar que los datos existen
        Serializable retrievedSaga = redisService.getSaga(orderId);
        assertNotNull(retrievedSaga, "La saga debería existir en Redis antes de eliminarla");

        // Eliminar los datos
        redisService.deleteSaga(orderId);

        // Validar que los datos fueron eliminados
        Serializable deletedSaga = redisService.getSaga(orderId);
        assertNull(deletedSaga, "La saga no debería existir en Redis después de eliminarla");
    }
}
