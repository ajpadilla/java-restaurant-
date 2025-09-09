package restaurant.order.order.domain;

import org.junit.jupiter.api.Test;
import restaurant.order.menu.domain.PlateMother;
import restaurant.order.menu.domain.Plate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CreateOrderAndDomainEventshould {

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        List<Plate> plates = List.of(PlateMother.random()); // create a dummy plate
        assertThrows(IllegalArgumentException.class, () -> Order.create(null, plates));
    }

    @Test
    void shouldThrowExceptionWhenPlatesIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Order(new OrderId("123"), null));
    }

    @Test
    void shouldThrowExceptionWhenPlatesIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Order(new OrderId("123"), List.of()));
    }

    @Test
    void shouldCreateOrderAndRecordEvent() {
        OrderId id = OrderIdMother.random();
        Plate plate = PlateMother.random(); // dummy plate
        Order order = Order.create(id, List.of(plate));

        assertEquals(id, order.getId());
        assertEquals(1, order.getPlates().size());
        assertEquals(plate, order.getPlates().get(0));

        var events = order.pullDomainEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof OrderCreatedDomainEvent);
        assertEquals(id.getValue(), ((OrderCreatedDomainEvent) events.get(0)).aggregateId());
    }

    @Test
    void shouldReturnImmutablePlatesList() {
        OrderId id = OrderIdMother.random();
        Plate plate = PlateMother.random(); // dummy plate
        Order order = new Order(id, List.of(plate));

        assertThrows(UnsupportedOperationException.class, () -> order.getPlates().add(PlateMother.random()));
    }

    @Test
    void shouldBeEqualWhenIdAndPlatesAreSame() {
        OrderId id = OrderIdMother.random();
        Plate plate = PlateMother.random(); // dummy plate

        Order order1 = new Order(id, List.of(plate));
        Order order2 = new Order(id, List.of(plate));

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdIsDifferent() {
        Plate plate = PlateMother.random(); // dummy plate
        Order order1 = new Order(OrderIdMother.random(), List.of(plate));
        Order order2 = new Order(OrderIdMother.random(), List.of(plate));

        assertNotEquals(order1, order2);
    }
}
