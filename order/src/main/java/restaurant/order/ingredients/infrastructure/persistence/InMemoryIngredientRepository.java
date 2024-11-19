package restaurant.order.ingredients.infrastructure.persistence;

import org.springframework.data.domain.Page;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.ingredients.domain.IngredientRepository;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryIngredientRepository implements IngredientRepository {
    private final HashMap<String, Ingredient> ingredients = new HashMap<>();

    @Override
    public void save(Ingredient ingredient) {
        this.ingredients.put(ingredient.getId().getValue(), ingredient);
    }

    @Override
    public Optional<Ingredient> search(IngredientId id) {
        return Optional.ofNullable(this.ingredients.get(id.getValue()));
    }

    @Override
    public Page<Ingredient> searchAll(int page, int size) {
        return null;
    }
}
