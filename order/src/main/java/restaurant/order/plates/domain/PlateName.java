package restaurant.order.plates.domain;

import restaurant.order.shared.domain.StringValueObject;

public class PlateName extends StringValueObject {
    public PlateName(String value) {
        super(value);
    }

    public PlateName() {
        super("");
    }

}
