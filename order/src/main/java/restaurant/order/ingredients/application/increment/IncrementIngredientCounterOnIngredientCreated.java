package restaurant.order.ingredients.application.increment;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.IngredientCreatedDomainEvent;

@Service
public class IncrementIngredientCounterOnIngredientCreated {
    @EventListener
    public void on(IngredientCreatedDomainEvent event) {
        System.out.println("New Event created");
    }
}
