package restaurant.order.order.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderMother;
import restaurant.order.order.domain.OrderRepository;
import restaurant.order.plates.domain.Plate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaOrderRepositoryShould {

    @Autowired
    protected OrderRepository jpaOrderRepository;

    @Test
    void SaveAPlateEntity() {
        Order order = OrderMother.random();

        this.jpaOrderRepository.save(order);

        Optional<Order> foundOrder = this.jpaOrderRepository.search(order.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals(order, foundOrder.get());
    }

    @Test
    void ReturnAnExistingPlate() {
        Order order = OrderMother.random();


        this.jpaOrderRepository.save(order);

        Optional<Order> foundOrder = this.jpaOrderRepository.search(order.getId());
        assertTrue(foundOrder.isPresent());

        Plate plate = foundOrder.get().getPlate();

        assertEquals(plate, order.getPlate());
        plate.getIngredients().forEach(System.out::println);
    }
}
