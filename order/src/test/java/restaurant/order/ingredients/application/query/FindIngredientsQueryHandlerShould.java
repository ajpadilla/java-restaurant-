package restaurant.order.ingredients.application.query;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import restaurant.order.ingredients.application.find.FindIngredientsQuery;
import restaurant.order.ingredients.application.find.FindIngredientsQueryHandler;
import restaurant.order.ingredients.application.find.ListOfIngredientResponse;
import restaurant.order.ingredients.application.find.IngredientsFinder;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.ingredients.domain.IngredientRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FindIngredientsQueryHandlerShould {

    @Autowired
    protected IngredientRepository jpaIngredientRepository;

    private FindIngredientsQueryHandler queryHandler;


    @Test
    void findAllIngredients() {

        int page = 0;
        int size = 10;

        Ingredient ingredient = IngredientMother.random();
        this.jpaIngredientRepository.save(ingredient);

        Ingredient ingredient2 = IngredientMother.random();
        this.jpaIngredientRepository.save(ingredient2);

        this.queryHandler = new FindIngredientsQueryHandler(new IngredientsFinder(this.jpaIngredientRepository));
        FindIngredientsQuery query = new FindIngredientsQuery(page, size);
        ListOfIngredientResponse response = this.queryHandler.handle(query);
        System.out.println(response.response().getTotalElements());
        System.out.println(response.response().getContent());
        assertNotNull(response.response().get());
    }

}
