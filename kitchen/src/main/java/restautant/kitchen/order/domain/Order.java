package restautant.kitchen.order.domain;


import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.shared.domain.AggregateRoot;

import java.util.Objects;

public class Order extends AggregateRoot {
    OrderId id;

    Plate plate;

    private OrderStatus status;

    public Order(OrderId id, Plate plate) {
        this.id = id;
        this.plate = plate;
        this.status = OrderStatus.PENDING;  // Estado inicial de la orden
    }

    public Order() {
        this.id = null;
        this.plate = null;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderId getId() {
        return id;
    }

    public Plate getPlate() {
        return plate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public static Order create(OrderId id, Plate plate) {
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", plate=" + plate +
                ", status=" + status +
                '}';
    }
}
