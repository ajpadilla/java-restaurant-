package restaurant.order.ingredients.application.find;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientRepository;

@Service
public class IngredientsFinder {
    private final IngredientRepository repository;

    public IngredientsFinder(IngredientRepository repository) {
        this.repository = repository;
    }

    public ListOfIngredientResponse find(int page, int size) {
        Page<Ingredient> ingredientList = this.repository.searchAll(page, size);
        return new ListOfIngredientResponse(ingredientList);
    }
}
