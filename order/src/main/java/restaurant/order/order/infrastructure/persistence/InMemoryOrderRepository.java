package restaurant.order.order.infrastructure.persistence;

import org.springframework.data.domain.Page;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderId;
import restaurant.order.order.domain.OrderRepository;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {
    private final HashMap<String, Order> orders = new HashMap<>();

    @Override
    public void save(Order order) {
        this.orders.put(order.getId().getValue(), order);
    }

    @Override
    public Optional<Order> search(OrderId id) {
        return Optional.ofNullable(this.orders.get(id.getValue()));
    }

    @Override
    public Page<Order> searchAll(int page, int size) {
        return null;
    }
}
