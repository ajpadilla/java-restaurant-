package restautant.kitchen.order.domain;


import restautant.kitchen.shared.test.UuidMother;

public class OrderIdMother {

    public static OrderId create(String value) {
        return new OrderId(value);
    }

    public static OrderId random() {
        return create(UuidMother.random());
    }

}
