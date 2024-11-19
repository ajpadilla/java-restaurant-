package restaurant.store.ingredients.domain;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IngredientRepository {

    void save(Ingredient ingredient);

    Optional<Ingredient> findByIdWithLock(IngredientId ingredientId);

    public Optional<Ingredient> searchByNameWithLock(String name);

    Optional<Ingredient> search(IngredientId id);

    void updateByName(String name, IngredientQuantity ingredient);

    Optional<Ingredient> searchByName(String name);  // Nuevo método

    Page<Ingredient> searchAll(int page, int size);

}
