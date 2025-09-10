package restaurant.order.order.application.find;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import restaurant.order.order.application.find.dto.OrderResponse;
import restaurant.order.order.domain.OrderRepository;
import restaurant.order.shared.cache.Cache;

@Service
public class OrdersFinder {
    private final OrderRepository repository;

    private final Cache cache;

    public OrdersFinder(OrderRepository repository, Cache cache) {
        this.repository = repository;
        this.cache = cache;
    }
    public Page<OrderResponse> find(int page, int size) {
        String key = "orders:page=" + page + ":size=" + size;

        Page<OrderResponse> cached = cache.get(
                key,
                new TypeReference<PageImpl<OrderResponse>>() {}
        );

        if (cached != null) return cached;

        Page<OrderResponse> ordersPage = repository.searchAll(page, size);
        cache.put(key, ordersPage, 300);

        return ordersPage;
    }
}
