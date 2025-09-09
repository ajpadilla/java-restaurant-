package restaurant.order.menu.infrastructure.api;

import org.springframework.stereotype.Service;
import  restaurant.order.menu.domain.IngredientId;
import  restaurant.order.menu.domain.Ingredient;
import restaurant.order.menu.domain.IngredientNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryInventoryClientService {

    // Simple in-memory storage
    private final Map<String, Ingredient> ingredientStore = new HashMap<>();

    // Save or update an ingredient
    public void saveIngredient(Ingredient ingredient) {
        ingredientStore.put(ingredient.getId().getValue(), ingredient);
    }

    // Find an ingredient by ID
    public Ingredient findIngredient(IngredientId id) {
        Ingredient ingredient = ingredientStore.get(id.getValue());
        if (ingredient == null) {
            throw new IngredientNotFoundException("Ingredient not found: " + id.getValue());
        }
        return ingredient;
    }

    // Get all ingredients
    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(ingredientStore.values());
    }

    // Delete an ingredient
    public void deleteIngredient(IngredientId id) {
        ingredientStore.remove(id);
    }

}
