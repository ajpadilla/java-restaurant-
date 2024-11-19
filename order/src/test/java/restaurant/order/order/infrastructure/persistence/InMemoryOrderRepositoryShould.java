package restaurant.order.order.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderIdMother;
import restaurant.order.order.domain.OrderMother;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryOrderRepositoryShould {

    protected InMemoryOrderRepository inMemoryOrderRepository;

    @BeforeEach
    protected void setUp() {
        this.inMemoryOrderRepository = new InMemoryOrderRepository();
    }

    @Test
    void saveOrder() {
        this.inMemoryOrderRepository.save(OrderMother.random());
    }

    @Test
    void ReturnAnExistingOrder() {
        Order order = OrderMother.random();
        this.inMemoryOrderRepository.save(order);
        assertEquals(Optional.of(order), this.inMemoryOrderRepository.search(order.getId()));
    }

    @Test
    void NotReturnANonExistingPlate() {
        assertFalse(this.inMemoryOrderRepository.search(OrderIdMother.random()).isPresent());
    }

}
