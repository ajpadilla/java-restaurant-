package restaurant.order.plates.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindPlatesQueryHandler implements QueryHandler<FindPlatesQuery, ListOfPlateResponse> {

    private final PlatesFinder finder;

    public FindPlatesQueryHandler(PlatesFinder finder) {
        this.finder = finder;
    }

    @Override
    public ListOfPlateResponse handle(FindPlatesQuery query) {
        return this.finder.find(query.getPage(), query.getSize());
    }
}
