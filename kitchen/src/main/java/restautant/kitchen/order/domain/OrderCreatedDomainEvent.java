package restautant.kitchen.order.domain;


import restautant.kitchen.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class OrderCreatedDomainEvent extends DomainEvent<OrderCreatedDomainEvent> {


    public OrderCreatedDomainEvent() {
        super(null);
    }

    public OrderCreatedDomainEvent(String aggregateId) {
        super(aggregateId);
    }
    public OrderCreatedDomainEvent(
            String aggregateId,
            String eventId,
            String occurredOn
    ) {
        super(aggregateId, eventId, occurredOn);
    }


    @Override
    public String eventName() {
        return  "order.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return new HashMap<String, Serializable>() {
        };
    }

    @Override
    public OrderCreatedDomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        return new OrderCreatedDomainEvent(
                aggregateId,
                eventId,
                occurredOn
        );
    }
}
