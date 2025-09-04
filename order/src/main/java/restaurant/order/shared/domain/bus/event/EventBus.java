package restaurant.order.shared.domain.bus.event;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventBus {
    @Transactional
    void publish(final List<DomainEvent> events);
}
