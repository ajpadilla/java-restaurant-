package restaurant.order.menu.domain;

import restaurant.order.shared.domain.StringValueObject;

public class PlateName extends StringValueObject {
    public PlateName(String value) {
        super(value);
    }

    public PlateName() {
        super("");
    }

}
