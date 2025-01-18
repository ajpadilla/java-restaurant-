package restaurant.store;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import restaurant.store.ingredients.domain.*;
import restaurant.store.ingredients.infrastructure.entity.IngredientEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }


    @Bean
    CommandLineRunner initializeIngredients(IngredientRepository ingredientRepository) {
        return args -> {
            final List<String> INGREDIENTS = Arrays.asList(
                    "Tomato", "Lemon", "Potato", "Rice", "Ketchup",
                    "Lettuce", "Onion", "Cheese", "Meat", "Chicken"
            );

            INGREDIENTS.forEach(ingredient -> {
                Ingredient ingredientObject = new Ingredient(
                        new IngredientId(UUID.randomUUID().toString()),
                        new IngredientName(ingredient),
                        new IngredientQuantity(5),
                        new IngredientReservedQuantity(0),
                        new IngredientVersion(0)
                );
                ingredientRepository.save(ingredientObject);
            });
        };
    }

}
