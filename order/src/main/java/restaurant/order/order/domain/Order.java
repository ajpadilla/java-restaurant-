package restaurant.order.order.domain;

import restaurant.order.plates.domain.Plate;
import restaurant.order.shared.domain.AggregateRoot;

import java.util.List;
import java.util.Objects;

public class Order extends AggregateRoot {
    OrderId id;

    List<Plate> plate;

    public Order(OrderId id, List<Plate> plate) {
        this.id = id;
        this.plate = plate;
    }

    public Order() {
        this.id = null;
        this.plate = null;
    }

    public OrderId getId() {
        return id;
    }

    public List<Plate> getPlates() {
        return plate;
    }

    public static Order create(OrderId id, List<Plate> plate) {
        Order order = new Order(id, plate);
        order.record(new OrderCreatedDomainEvent(id.getValue()));
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(plate, order.plate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate);
    }
}
