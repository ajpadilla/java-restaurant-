package restautant.kitchen.order.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredients.domain.IngredientMother;
import restautant.kitchen.order.application.OrderStatusService;
import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.plate.domain.PlateIdMother;
import restautant.kitchen.plate.domain.PlateMother;
import restautant.kitchen.plate.domain.PlateNameMother;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderStatusServiceTest {
    @Autowired
    protected OrderRepository jpaOrderRepository;

    @Autowired
    OrderStatusService orderStatusService;

    @Test
    void testGetOrderStatusFromDatabase_OrderFound() {

        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(IngredientMother.random());
        }

        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            plates.add(PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients));
        }

        Order order = OrderMother.create(OrderIdMother.random(), plates);

        assertEquals(OrderStatus.PENDING, order.getStatus(), "El estado de la orden debería ser PENDING");

        order.markAsReady();

        this.jpaOrderRepository.save(order);

        Optional<Order> orderPresentRetrieve = this.jpaOrderRepository.search(order.getId());

        Order orderRetrieved = orderPresentRetrieve.get();

        System.out.println("OrderRetrieved" + orderRetrieved);

        // Verificamos que el resultado es el esperado
        assertTrue(orderPresentRetrieve.isPresent(), "El estado de la orden debería estar presente");
        assertEquals(OrderStatus.READY, orderRetrieved.getStatus(), "El estado de la orden debería ser PENDING");
    }
}
