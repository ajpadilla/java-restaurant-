package restaurant.order.ingredients.domain;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IngredientRepository {

    void save(Ingredient ingredient);

    Optional<Ingredient> search(IngredientId id);

    Page<Ingredient> searchAll(int page, int size);

}
