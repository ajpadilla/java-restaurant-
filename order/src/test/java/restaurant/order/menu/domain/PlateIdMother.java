package restaurant.order.menu.domain;

import restaurant.order.shared.test.UuidMother;

public class PlateIdMother {

    public static PlateId create(String value) {
        return new PlateId(value);
    }

    public static PlateId random() {
        return create(UuidMother.random());
    }

}
