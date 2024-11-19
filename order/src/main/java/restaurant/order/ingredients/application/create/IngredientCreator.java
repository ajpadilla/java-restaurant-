package restaurant.order.ingredients.application.create;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.*;
import restaurant.order.shared.domain.bus.event.EventBus;

@Service
public class IngredientCreator {

    private final IngredientRepository repository;

    private final EventBus eventBus;


    public IngredientCreator(IngredientRepository repository, @Qualifier("postgreSqlEventBus") EventBus eventBus) {

        this.repository = repository;
        this.eventBus = eventBus;
    }

    public void create(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        Ingredient ingredient = Ingredient.create(id, name, quantity);
        this.repository.save(ingredient);
        this.eventBus.publish(ingredient.pullDomainEvents());
    }

}
