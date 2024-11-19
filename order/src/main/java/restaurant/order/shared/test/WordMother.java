package restaurant.order.shared.test;

public final class WordMother {
    public static String random() {
        return MotherCreator.random().lorem().word();
    }
}
