package restaurant.order.order.domain;

import org.springframework.data.domain.Page;
import restaurant.order.order.application.find.dto.OrderResponse;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> search(OrderId id);

    Page<OrderResponse> searchAll(int page, int size);

}
