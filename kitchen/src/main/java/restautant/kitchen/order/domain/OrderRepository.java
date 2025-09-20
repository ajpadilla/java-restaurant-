package restautant.kitchen.order.domain;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> search(OrderId id);

    Page<Order> searchAll(int page, int size);

}
