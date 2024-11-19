package restaurant.order.ingredients.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Createingredientanddomaineventshould {

    @Test
    public void testCreateIngredient() {
        IngredientId id = IngredientIdMother.random();
        IngredientName name = IngredientNameMother.random();
        IngredientQuantity quantity = IngredientQuantityMother.random();

        Ingredient ingredient = Ingredient.create(id, name, quantity);

        assertNotNull(ingredient);
        assertEquals(id.getValue(), ingredient.getId().getValue());
        assertEquals(name.getValue(), ingredient.getName().getValue());
        assertEquals(quantity.getValue(), ingredient.getQuantity().getValue());

        IngredientCreatedDomainEvent event = (IngredientCreatedDomainEvent) ingredient.pullDomainEvents().get(0);
        assertNotNull(event);

        assertEquals(ingredient.getId().getValue(), event.aggregateId());
        assertEquals(ingredient.getName().getValue(), event.getName());
        assertEquals(ingredient.getQuantity().getValue(), event.getQuantity());

    }

}
