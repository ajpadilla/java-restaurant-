package restautant.kitchen.order.domain;


import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.shared.domain.AggregateRoot;

import java.util.List;
import java.util.Objects;

public class Order extends AggregateRoot {
    OrderId id;

    List<Plate> plates;

    private OrderStatus status;

    public Order(OrderId id, List<Plate> plate) {
        this.id = id;
        this.plates = plate;
        this.status = OrderStatus.PENDING;  // Estado inicial de la orden
    }
    public Order(OrderId id, List<Plate> plate, OrderStatus status) {
        this.id = id;
        this.plates = plate;
        this.status = status;
    }

    public Order() {
        this.id = null;
        this.plates = null;
    }

    public OrderId getId() {
        return id;
    }

    public  List<Plate> getPlates() {
        return plates;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public static Order create(OrderId id, List<Plate> plate) {
        Order order = new Order(id, plate);
        order.record(new OrderCreatedDomainEvent(id.getValue()));
        return order;
    }

    public void markAsReady() {
        System.out.println("OrderStatus:" + OrderStatus.PENDING);
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order must be in PENDING status to be confirmed.");
        }
        this.status = OrderStatus.READY;
        //this.record(new OrderStatusChangedDomainEvent(this.id.getValue(), this.status.name()));
    }

    public void markAsCancelled() {
        if (this.status == OrderStatus.READY) {
            throw new IllegalStateException("Confirmed orders cannot be cancelled.");
        }
        this.status = OrderStatus.CANCELLED;
        //this.record(new OrderStatusChangedDomainEvent(this.id.getValue(), this.status.name()));
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(plates, order.plates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plates);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", plate=" + plates +
                ", status=" + status +
                '}';
    }
}
