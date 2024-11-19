package restautant.kitchen.ingredient.domain;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IngredientRepository {

    void save(Ingredient ingredient);

    Page<Ingredient> searchAll(int page, int size);

}
