package restaurant.order.plate.domain;

import restaurant.order.plates.domain.PlateId;
import restaurant.order.shared.test.UuidMother;

public class PlateIdMother {

    public static PlateId create(String value) {
        return new PlateId(value);
    }

    public static PlateId random() {
        return create(UuidMother.random());
    }

}
