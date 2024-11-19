package restaurant.order.order.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindOrdersQueryHandler implements QueryHandler<FindOrdersQuery, ListOfOrdersResponse> {
    private final OrdersFinder finder;

    public FindOrdersQueryHandler(OrdersFinder finder) {
        this.finder = finder;
    }

    @Override
    public ListOfOrdersResponse handle(FindOrdersQuery query) {
        return this.finder.find(query.getPage(), query.getSize());
    }
}
