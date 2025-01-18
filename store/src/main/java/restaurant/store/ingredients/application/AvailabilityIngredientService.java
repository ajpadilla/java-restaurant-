package restaurant.store.ingredients.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.store.ingredients.domain.*;
import restaurant.store.ingredients.infrastructure.api.IngredientPurchaseService;
import restaurant.store.order.infrastructure.request.CreateOrderRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AvailabilityIngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientPurchaseService purchaseService;


    public HashMap<String, Serializable> processOrder(CreateOrderRequest request) {

        HashMap<String, Serializable> orderDetailMap = new HashMap<>();

        List<HashMap<String, Serializable>> platesProcessedList = new ArrayList<>();


        orderDetailMap.put("order_id", request.getOrderId());
        request.getPlates().forEach(plateRequest -> {

            List<HashMap<String, Serializable>> ingredientsProcessedList = new ArrayList<>();


            HashMap<String, Serializable> processedIngredients = new HashMap<>();
            HashMap<String, Serializable> processedPlate = new HashMap<>();

            plateRequest.getIngredients().forEach(ingredientRequest -> {
                IngredientName ingredientName = new IngredientName(ingredientRequest.getName());
                int maxRetries = 5;
                int attempts = 0;

                while (attempts < maxRetries)
                {
                    try {
                        Ingredient foundIngredient = getIngredient(ingredientName);

                        if (foundIngredient.getQuantity().getValue() >= ingredientRequest.getQuantity()) {
                            foundIngredient.setQuantity(new IngredientQuantity(
                                    foundIngredient.getQuantity().getValue() - ingredientRequest.getQuantity())
                            );
                            ingredientRepository.updateByName(
                                    ingredientName.getValue(),
                                    foundIngredient.getQuantity()
                            );
                            processedIngredients.put(ingredientRequest.getName(), ingredientRequest.getQuantity());
                            break; // Salir si la operación es exitosa
                        } else {
                            // Solicitar compra de ingrediente
                            int quantity = purchaseService.requestIngredientPurchase(
                                    ingredientName.getValue(),
                                    ingredientRequest.getQuantity()
                            );
                            ingredientRepository.updateByName(
                                    ingredientName.getValue(),
                                    new IngredientQuantity(foundIngredient.getQuantity().getValue() + quantity)
                            );
                        }
                    } catch (Exception ex) {
                        System.err.println("Error processing ingredient: " + ingredientName.getValue());
                        System.err.println("Error message: " + ex.getMessage());
                    }

                    attempts++;
                    if (attempts == maxRetries) {
                        throw new IllegalStateException(
                                "Unable to process ingredient after " + maxRetries + " attempts"
                        );
                    }

                    try {
                        Thread.sleep(100); // Espera breve entre intentos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted", e);
                    }
                }
            });

            ingredientsProcessedList.add(processedIngredients);


            processedPlate.put("plate_id", plateRequest.getPlateId());
            processedPlate.put("plate_name", plateRequest.getPlateName());
            processedPlate.put("plate_ingredients", (Serializable) ingredientsProcessedList);
            platesProcessedList.add(processedPlate);

        });
        orderDetailMap.put("plate", (Serializable) platesProcessedList);

        return orderDetailMap;
    }

    private Ingredient getIngredient(IngredientName ingredientName) {
        return this.ingredientRepository.searchByName(ingredientName.getValue())
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found"));
    }

}
