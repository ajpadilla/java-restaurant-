package restaurant.order.menu.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.IngredientCreatedDomainEvent;

@Service
public class IngredientMetricsSubscriber {

    private final Counter ingredientsCreated;
    private final Counter ingredientsFailed; // optional, if you also emit failure events

    public IngredientMetricsSubscriber(MeterRegistry registry) {
        this.ingredientsCreated = Counter.builder("ingredients_created_total")
                .description("Total number of successfully created ingredients")
                .register(registry);

        this.ingredientsFailed = Counter.builder("ingredients_failed_total")
                .description("Total number of failed ingredient creations")
                .register(registry);
    }

    @EventListener
    public void handleIngredientCreated(IngredientCreatedDomainEvent event) {
        ingredientsCreated.increment();
        // optional: log or do other things with event.getName(), event.getQuantity()
    }

}
