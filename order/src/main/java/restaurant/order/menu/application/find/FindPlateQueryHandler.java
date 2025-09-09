package restaurant.order.menu.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindPlateQueryHandler implements QueryHandler<FindPlateQuery, PlateResponse> {

    private final PlateFinder finder;

    public FindPlateQueryHandler(PlateFinder finder) {
        this.finder = finder;
    }

    @Override
    public PlateResponse handle(FindPlateQuery query) {
       return this.finder.find(new PlateId(query.getId()));
    }
}
