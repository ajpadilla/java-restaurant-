package restaurant.order.order.domain;

import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class OrderCreatedDomainEvent extends DomainEvent<OrderCreatedDomainEvent> {

    private final List<String> plateIds;

    public OrderCreatedDomainEvent() {
        super(null);
        this.plateIds = Collections.emptyList();
    }

    public OrderCreatedDomainEvent(String aggregateId, List<String> plateIds) {
        super(aggregateId);
        this.plateIds = plateIds;
    }
    public OrderCreatedDomainEvent(
            String aggregateId,
            String eventId,
            String occurredOn,
            List<String> plateIds
    ) {
        super(aggregateId, eventId, occurredOn);
        this.plateIds = plateIds;
    }


    @Override
    public String eventName() {
        return  "order.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        HashMap<String, Serializable> map = new HashMap<>();
        map.put("plateIds", (Serializable) plateIds);
        return map;
    }

    @Override
    public OrderCreatedDomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        List<String> platesIds = (List<String>) body.get("plateIds");

        return new OrderCreatedDomainEvent(
                aggregateId,
                eventId,
                occurredOn,
                platesIds
        );
    }
}
