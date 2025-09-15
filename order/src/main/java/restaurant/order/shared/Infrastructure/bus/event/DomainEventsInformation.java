package restaurant.order.shared.Infrastructure.bus.event;


import org.springframework.stereotype.Service;
import restaurant.order.menu.domain.PlateCreateDomainEvent;
import restaurant.order.order.domain.OrderCreatedDomainEvent;
import restaurant.order.shared.domain.bus.event.DomainEvent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public final class DomainEventsInformation {

    private static final Logger log = LoggerFactory.getLogger(DomainEventsInformation.class);
    private final HashMap<String, Class<? extends DomainEvent>> indexedDomainEvents = new HashMap<>();

    public DomainEventsInformation() {
        // ✅ Define the events directly here
        List<Class<? extends DomainEvent>> eventClasses = List.of(
                PlateCreateDomainEvent.class,
                OrderCreatedDomainEvent.class
        );

        for (Class<? extends DomainEvent> domainEventClass : eventClasses) {
            try {
                DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();
                Method eventNameMethod = domainEventClass.getMethod("eventName");
                String eventName = (String) eventNameMethod.invoke(nullInstance);

                indexedDomainEvents.put(eventName, domainEventClass);

            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to register domain event class: " + domainEventClass.getName(), e
                );
            }
        }

        // ✅ Log all registered events
        log.info("Registered domain events: {}", indexedDomainEvents.keySet());
    }

    public Class<? extends DomainEvent> forName(String name) {
        return indexedDomainEvents.get(name);
    }

    public String forClass(Class<? extends DomainEvent> domainEventClass) {
        return indexedDomainEvents.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), domainEventClass))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }
}