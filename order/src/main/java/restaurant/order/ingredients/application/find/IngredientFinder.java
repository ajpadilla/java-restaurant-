package restaurant.order.ingredients.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.ingredients.domain.IngredientNotFoundException;
import restaurant.order.ingredients.domain.IngredientRepository;

@Service
public class IngredientFinder {

    private final IngredientRepository repository;

    public IngredientFinder(IngredientRepository repository) {
        this.repository = repository;
    }

    public IngredientResponse find(IngredientId id) {
        Ingredient ingredient = this.repository.search(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found: " + id.getValue()));
        return new IngredientResponse(ingredient);
    }
}
