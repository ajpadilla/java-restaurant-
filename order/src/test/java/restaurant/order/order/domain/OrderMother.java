package restaurant.order.order.domain;

import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plates.domain.Plate;

public class OrderMother {

    public static Order create(OrderId id, Plate plate) {
        return  new Order(id, plate);
    }

    public static Order random() {
        return create(OrderIdMother.random(), PlateMother.random());
    }

}
