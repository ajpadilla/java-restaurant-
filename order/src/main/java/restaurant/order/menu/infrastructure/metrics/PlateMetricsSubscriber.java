package restaurant.order.menu.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import restaurant.order.menu.domain.PlateCreateDomainEvent;

@Service
public class PlateMetricsSubscriber {

    private final Counter platesCreated;
    private final Counter platesFailed; // optional, if you also emit failure events

    public PlateMetricsSubscriber(MeterRegistry registry) {
        this.platesCreated = Counter.builder("plates_created_total")
                .description("Total number of successfully created plates")
                .register(registry);

        this.platesFailed = Counter.builder("plates_failed_total")
                .description("Total number of failed plates")
                .register(registry);
    }

    @EventListener
    public void handleIngredientCreated(PlateCreateDomainEvent event) {
        platesCreated.increment();
        // optional: log or do other things with event.getName(), event.getQuantity()
    }

}
