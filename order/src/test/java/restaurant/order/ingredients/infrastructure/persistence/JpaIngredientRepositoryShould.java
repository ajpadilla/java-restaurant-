package restaurant.order.ingredients.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.ingredients.domain.IngredientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class JpaIngredientRepositoryShould {

    @Autowired
    protected IngredientRepository jpaIngredientRepository;

    @Test
    void SaveAIngredientEntity() {
        this.jpaIngredientRepository.save(IngredientMother.random());
    }

    @Test
    void ReturnAnExistingIngredient() {
        Ingredient ingredient = IngredientMother.random();
        this.jpaIngredientRepository.save(ingredient);
        assertEquals(Optional.of(ingredient), this.jpaIngredientRepository.search(ingredient.getId()));
    }
}
