package restaurant.order.shared.test;

public final class IntegerMother {

    public static Integer random() {
        return MotherCreator.random().number().numberBetween(1, 5);
    }


}
