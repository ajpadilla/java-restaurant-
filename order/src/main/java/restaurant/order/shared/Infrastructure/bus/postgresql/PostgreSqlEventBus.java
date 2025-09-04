package restaurant.order.shared.Infrastructure.bus.postgresql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.Utils;
import restaurant.order.shared.domain.bus.event.DomainEvent;
import restaurant.order.shared.domain.bus.event.EventBus;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Service
public class  PostgreSqlEventBus implements EventBus {
    private final EntityManager entityManager;

    public PostgreSqlEventBus(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional

    public void publish(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

    private void publish(DomainEvent domainEvent) {
        String id = domainEvent.eventId();
        String aggregateId = domainEvent.aggregateId();
        String name = domainEvent.eventName();
        HashMap<String, Serializable> body = domainEvent.toPrimitives();
        String occurredOn = domainEvent.occurredOn();

        Query query = entityManager.createNativeQuery(
                "INSERT INTO domain_events (id, aggregate_id, name, body, occurred_on, consumed) " +
                        "VALUES (:id, :aggregateId, :name, :body, :occurredOn, :consumed)"
        );

        query.setParameter("id", id);
        query.setParameter("aggregateId", aggregateId);
        query.setParameter("name", name);
        query.setParameter("body", Utils.jsonEncode(body));
        query.setParameter("occurredOn", occurredOn);
        query.setParameter("consumed", false);

        query.executeUpdate();
    }

    public List<Object[]> findAllEvents() {
        String sql = "SELECT id, aggregate_id, name, body, occurred_on FROM domain_events";
        return entityManager.createNativeQuery(sql).getResultList();
    }

}
