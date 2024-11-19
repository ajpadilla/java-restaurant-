package restaurant.order.plates.application.find;

import org.springframework.data.domain.Page;
import restaurant.order.plates.domain.Plate;
import restaurant.order.shared.domain.bus.query.Response;

public class ListOfPlateResponse implements Response {

    private final Page<Plate> plates;

    public ListOfPlateResponse(Page<Plate> plates) {
        this.plates = plates;
    }

    public Page<Plate> response() {
        return plates;
    }
}
