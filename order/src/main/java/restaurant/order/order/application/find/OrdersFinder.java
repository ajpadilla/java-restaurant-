package restaurant.order.order.application.find;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import restaurant.order.order.application.find.dto.OrderResponse;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderRepository;

@Service
public class OrdersFinder {
    private final OrderRepository repository;
    public OrdersFinder(OrderRepository repository) {
        this.repository = repository;
    }
    public Page<OrderResponse> find(int page, int size) {
        return this.repository.searchAll(page, size);
    }
}
