package restaurant.order.order.application.find;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import restaurant.order.order.application.find.dto.OrderResponse;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindOrdersQueryHandler implements QueryHandler<FindOrdersQuery, Page<OrderResponse>> {
    private final OrdersFinder finder;

    public FindOrdersQueryHandler(OrdersFinder finder) {
        this.finder = finder;
    }

    @Override
    public Page<OrderResponse> handle(FindOrdersQuery query) {
        return this.finder.find(query.getPage(), query.getSize());
    }
}
