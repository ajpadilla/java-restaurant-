package restaurant.order.order.domain;

import restaurant.order.plates.domain.Plate;
import restaurant.order.shared.domain.AggregateRoot;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Order extends AggregateRoot {
    private final OrderId id;

    private final List<Plate> plates;

    public Order(OrderId id, List<Plate> plate) {
        this.id = id;
        this.plates = List.copyOf(plate) ;
    }

    public Order() {
        this.id = null;
        this.plates = List.of();
    }

    public OrderId getId() {
        return id;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public static Order create(OrderId id, List<Plate> plates) {
        validateOrderIdNotNull(id);
        validatePlatesNotEmpty(plates);
        validateMaxPlates(plates);
        validateNoDuplicates(plates);

        Order order = new Order(id, plates);
        order.record(new OrderCreatedDomainEvent(id.getValue()));
        return order;
    }


    private static void validateOrderIdNotNull(OrderId id) {
        if (id == null) {
            throw new IllegalArgumentException("OderId couldn't be empty");
        }
    }

    private static void validatePlatesNotEmpty(List<Plate> plates) {
        if (plates == null || plates.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one plate");
        }
    }

    private static void validateMaxPlates(List<Plate> plates) {
        if (plates.size() > 5) {
            throw new IllegalArgumentException("An order cannot have more than 5 plates");
        }
    }

    private static void validateNoDuplicates(List<Plate> plates) {
        Set<Plate> uniquePlates = new HashSet<>(plates);
        if (uniquePlates.size() < plates.size()) {
            throw new IllegalArgumentException("Order cannot contain duplicate plates");
        }
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
}
