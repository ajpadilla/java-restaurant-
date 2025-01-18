package restaurant.order.shared.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class WordMother {
    private static final List<String> INGREDIENTS = Arrays.asList(
            "Tomato", "Lemon", "Potato", "Rice", "Ketchup",
            "Lettuce", "Onion", "Cheese", "Meat", "Chicken"
    );

    private static final Random RANDOM = new Random();

    public static String random() {
        // Selecciona un ingrediente aleatorio de la lista
        return INGREDIENTS.get(RANDOM.nextInt(INGREDIENTS.size()));
    }
}
