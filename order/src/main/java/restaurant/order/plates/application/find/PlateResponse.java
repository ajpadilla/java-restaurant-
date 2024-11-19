package restaurant.order.plates.application.find;

import restaurant.order.plates.domain.Plate;
import restaurant.order.shared.domain.bus.query.Response;

public class PlateResponse implements Response {

    private final Plate plate;


    public PlateResponse(Plate plate) {
        this.plate = plate;
    }

    public Plate getPlate() {
        return plate;
    }
}
