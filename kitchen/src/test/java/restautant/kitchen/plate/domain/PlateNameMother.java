package restautant.kitchen.plate.domain;

import restautant.kitchen.shared.test.WordMother;

public class PlateNameMother {
    public static PlateName create(String value) {
        return new PlateName(value);
    }

    public static PlateName random() {
        return create(WordMother.random());
    }
}
