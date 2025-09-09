package restaurant.order.menu.application.increment;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import restaurant.order.menu.domain.PlateCreateDomainEvent;

@Service
public class IncrementPlateCounterOnPlateCreated {
    @EventListener
    public void on(PlateCreateDomainEvent event) {
        System.out.println("New Plate created");
    }
}
