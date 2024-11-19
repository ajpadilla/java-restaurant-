package restaurant.order.ingredients.domain;

import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.InvalidUUIDException;
import restaurant.order.shared.domain.Utils;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient findAnExistingIngredient(IngredientId id) {
        if (!Utils.isUUID(id.getValue())) {
            throw new InvalidUUIDException("Invalid UUID string: " + id.getValue());
        }

        return this.ingredientRepository.search(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found: " + id.getValue()));
    }
}
