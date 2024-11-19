package restaurant.order.plate.domain;

import restaurant.order.plates.domain.PlateName;
import restaurant.order.shared.test.WordMother;

public class PlateNameMother {
    public static PlateName create(String value) {
        return new PlateName(value);
    }

    public static PlateName random() {
        return create(WordMother.random());
    }
}
