package restaurant.order.ingredients.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientIdMother;
import restaurant.order.ingredients.domain.IngredientMother;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class InMemoryIngredientRepositoryShould {
    protected InMemoryIngredientRepository inMemoryCourseRepository;

    @BeforeEach
    protected void setUp() {
        this.inMemoryCourseRepository = new InMemoryIngredientRepository();
    }

    @Test
    void SaveAIngredient() {
        Ingredient ingredient = IngredientMother.random();
        this.inMemoryCourseRepository.save(ingredient);
    }

    @Test
    void ReturnAnExistingCourse() {
        Ingredient ingredient = IngredientMother.random();
        this.inMemoryCourseRepository.save(ingredient);
        assertEquals(Optional.of(ingredient), this.inMemoryCourseRepository.search(ingredient.getId()));
    }
    @Test
    void NotReturnANonExistingIngredient() {
        assertFalse(this.inMemoryCourseRepository.search(IngredientIdMother.random()).isPresent());
    }
}
