package restautant.kitchen.shared.test;

import com.github.javafaker.Faker;

public final class MotherCreator {
    private final static Faker faker = new Faker();

    public static Faker random() {
        return faker;
    }
}
