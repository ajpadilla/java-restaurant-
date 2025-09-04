package restaurant.order.shared.Infrastructure.bus.postgresql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.shared.Infrastructure.bus.event.DomainEventsInformation;
import restaurant.order.shared.domain.Utils;
import restaurant.order.shared.domain.bus.event.DomainEvent;
import restaurant.order.shared.domain.bus.event.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class PostgreSqlConsumer {

    @PersistenceContext
    private final EntityManager entityManager;
    private final DomainEventsInformation domainEventsInformation;

    private final EventBus bus;
    private final Integer CHUNKS = 200;

    public PostgreSqlConsumer(
            EntityManager entityManager,
            DomainEventsInformation domainEventsInformation,
            @Qualifier("springApplicationEventBus") EventBus bus
    ) {
        this.entityManager = entityManager;
        this.domainEventsInformation = domainEventsInformation;
        this.bus = bus;
    }

    /**
     * Scheduled polling: polls every 2 seconds (configurable)
     */
    @Scheduled(fixedDelayString = "${events.polling.delay:200000}")
    @Transactional
    public void pollEvents() {
        consumeChunk();
    }

    @Transactional
    public void consumeChunk() {
        Query query = entityManager.createNativeQuery(
                "SELECT id, aggregate_id, name, body, occurred_on " +
                        "FROM domain_events " +
                        "WHERE consumed = FALSE " +
                        "ORDER BY occurred_on ASC " +
                        "LIMIT :chunk"
        );
        query.setParameter("chunk", CHUNKS);

        List<Object[]> events = query.getResultList();
        if (events.isEmpty()) return;

        for (Object[] row : events) {
            try {
                DomainEvent domainEvent = buildDomainEvent(row);
                publishEventAsync(domainEvent);
                markAsConsumed((String) row[0]);
            } catch (Exception e) {
                e.printStackTrace(); // optional: add logging for failed events
            }
        }

        entityManager.clear(); // free memory
    }

    private DomainEvent buildDomainEvent(Object[] row) throws Exception {
        String id = (String) row[0];
        String aggregateId = (String) row[1];
        String eventName = (String) row[2];
        String body = (String) row[3];
        String occurredOnStr = (String) row[4];
        Timestamp occurredOn = Timestamp.valueOf(occurredOnStr + " 00:00:00");

        Class<? extends DomainEvent> domainEventClass = domainEventsInformation.forName(eventName);
        DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();

        return (DomainEvent) domainEventClass
                .getMethod("fromPrimitives", String.class, HashMap.class, String.class, String.class)
                .invoke(nullInstance, aggregateId, Utils.jsonDecode(body), id, Utils.dateToString(occurredOn));
    }

    @Async
    protected void publishEventAsync(DomainEvent event) {
        bus.publish(Collections.singletonList(event));
    }

    @Transactional
    public void markAsConsumed(String eventId) {
        Query q = entityManager.createNativeQuery(
                "UPDATE domain_events SET consumed = TRUE WHERE id = :id"
        );
        q.setParameter("id", eventId);
        q.executeUpdate();
    }

}
