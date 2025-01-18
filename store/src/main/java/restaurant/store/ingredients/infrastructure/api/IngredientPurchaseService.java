package restaurant.store.ingredients.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.store.purchases.domain.*;

import java.util.Random;
import java.util.UUID;

@Service
public class IngredientPurchaseService {

    private final Random random;

    @Autowired
    private PurchaseRepository purchaseRepository;


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
        this.purchaseRepository.save(new Purchase(
                new PurchaseId(UUID.randomUUID().toString()),
                new PurchaseDescription("Buying" + ingredientName + " " + purchasedQuantity),
                new PurchaseQuantity(purchasedQuantity)
        ));
        System.out.println("Purchased " + purchasedQuantity + " units of ingredient: " + ingredientName);
        return purchasedQuantity;
    }

}
