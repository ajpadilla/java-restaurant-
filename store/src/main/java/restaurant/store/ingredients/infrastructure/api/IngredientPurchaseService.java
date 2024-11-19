package restaurant.store.ingredients.infrastructure.api;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class IngredientPurchaseService {

    private final Random random;

    public IngredientPurchaseService() {
        this.random = new Random();
    }

    /**
     * Simula una petición hacia una API externa para comprar ingredientes.
     * Devuelve un número aleatorio entre 0 y 5.
     */
    public int requestIngredientPurchase(String ingredientName, int requestedQuantity) {
        System.out.println("Requesting purchase for ingredient: " + ingredientName +
                ", requested quantity: " + requestedQuantity);

        // Simular una respuesta de la API con una cantidad aleatoria de ingredientes disponibles
        int purchasedQuantity = random.nextInt(6); // Devuelve un número entre 0 y 5
        System.out.println("Purchased " + purchasedQuantity + " units of ingredient: " + ingredientName);

        return purchasedQuantity;
    }

}
