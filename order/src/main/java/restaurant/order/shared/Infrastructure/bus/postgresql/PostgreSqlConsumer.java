package restaurant.order.shared.Infrastructure.bus.postgresql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private volatile Boolean shouldStop = false;

    public PostgreSqlConsumer(
            EntityManager entityManager,
            DomainEventsInformation domainEventsInformation,
            @Qualifier("springApplicationEventBus") EventBus bus
    ) {
        this.entityManager = entityManager;
        this.domainEventsInformation = domainEventsInformation;
        this.bus = bus;
    }

    @Transactional
    public void consume() {
        int offset = 0;
        while (!this.shouldStop) {
            Query query = entityManager.createNativeQuery(
                    "SELECT id, aggregate_id, name, body, occurred_on FROM domain_events ORDER BY occurred_on ASC LIMIT :chunk OFFSET :offset"
            );
            query.setParameter("chunk", CHUNKS);
            query.setParameter("offset", offset);

            List<Object[]> events = query.getResultList();

            if (events.isEmpty()) {
                break;
            }


            try {
                for (Object[] event : events) {

                    System.out.println("Event ID: " + event[0]);
                    System.out.println("Aggregate ID: " + event[1]);
                    System.out.println("Event Name: " + event[2]);
                    System.out.println("Event Body: " + event[3]);
                    System.out.println("Occurred On: " + event[4]);

                    executeSubscribers(
                            (String) event[0],
                            (String) event[1],
                            (String) event[2],
                            (String) event[3],
                            Timestamp.valueOf((String) (event[4] += " 00:00:00" ))
                    );
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                     InstantiationException e) {
                e.printStackTrace();
            }
            offset += CHUNKS;
            entityManager.clear();
        }
    }

    public void stop() {
        this.shouldStop = true;
    }

    private void executeSubscribers(
            String id, String aggregateId, String eventName, String body, Timestamp occurredOn
    ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<? extends DomainEvent> domainEventClass = this.domainEventsInformation.forName(eventName);

        DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();

        Method fromPrimitivesMethod = domainEventClass.getMethod(
                "fromPrimitives",
                String.class,
                HashMap.class,
                String.class,
                String.class
        );

        Object domainEvent = fromPrimitivesMethod.invoke(
                nullInstance,
                aggregateId,
                Utils.jsonDecode(body),
                id,
                Utils.dateToString(occurredOn)
        );

        this.bus.publish(Collections.singletonList((DomainEvent) domainEvent));
    }

}
