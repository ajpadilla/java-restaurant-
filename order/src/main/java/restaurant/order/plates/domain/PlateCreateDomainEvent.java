package restaurant.order.plates.domain;

import restaurant.order.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class PlateCreateDomainEvent extends DomainEvent<PlateCreateDomainEvent> {

    private final String name;

    public PlateCreateDomainEvent() {
        super(null);
        this.name = null;
    }

    public PlateCreateDomainEvent(String aggregateId, String name) {
        super(aggregateId);
        this.name = name;
    }
    public PlateCreateDomainEvent(
            String aggregateId,
            String eventId,
            String occurredOn,
            String name
    ) {
        super(aggregateId, eventId, occurredOn);
        this.name = name;
    }


    @Override
    public String eventName() {
        return  "plate.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return new HashMap<String, Serializable>() {{
            put("name", name);
        }};
    }

    @Override
    public PlateCreateDomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        return new PlateCreateDomainEvent(
                aggregateId,
                eventId,
                occurredOn,
                (String) body.get("name")
        );
    }
}
