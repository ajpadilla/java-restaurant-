package restaurant.order.menu.application.find;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindPlatesQueryHandler implements QueryHandler<FindPlatesQuery,  Page<PlateResponse>> {

    private final PlatesFinder finder;

    public FindPlatesQueryHandler(PlatesFinder finder) {
        this.finder = finder;
    }

    @Override
    public Page<PlateResponse> handle(FindPlatesQuery query) {
        return this.finder.find(query.getPage(), query.getSize());
    }
}
