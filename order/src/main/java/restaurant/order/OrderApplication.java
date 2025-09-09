package restaurant.order;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import restaurant.order.menu.domain.Ingredient;
import restaurant.order.menu.domain.IngredientId;
import restaurant.order.menu.domain.IngredientName;
import restaurant.order.menu.domain.IngredientQuantity;
import restaurant.order.menu.infrastructure.api.InMemoryInventoryClientService;

@SpringBootApplication
@EnableScheduling
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
    @Bean
    CommandLineRunner loadIngredients(InMemoryInventoryClientService ingredientService) {
        return args -> {
            ingredientService.saveIngredient(
                    new Ingredient(new IngredientId("11111111-1111-1111-1111-111111111111"),
                            new IngredientName("Tomato"),
                            new IngredientQuantity(5))
            );
            ingredientService.saveIngredient(
                    new Ingredient(new IngredientId("22222222-2222-2222-2222-222222222222"),
                            new IngredientName("Cheese"),
                            new IngredientQuantity(3))
            );
            System.out.println("Preloaded ingredients: " + ingredientService.getAllIngredients());
        };
    }
}


