package restautant.kitchen.plate.domain;


import restautant.kitchen.shared.test.UuidMother;

public class PlateIdMother {

    public static PlateId create(String value) {
        return new PlateId(value);
    }

    public static PlateId random() {
        return create(UuidMother.random());
    }

}
