package restaurant.order.menu.domain;

import restaurant.order.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlateCreateDomainEvent extends DomainEvent<PlateCreateDomainEvent> {

    private final String name;

    private final List<String> ingredientIds;

    public PlateCreateDomainEvent() {
        super(null);
        this.name = null;
        this.ingredientIds = Collections.emptyList();
    }

    public PlateCreateDomainEvent(String aggregateId, String name, List<String> IngredientId) {
        super(aggregateId);
        this.name = name;
        this.ingredientIds = IngredientId;
    }
    public PlateCreateDomainEvent(
            String aggregateId,
            String eventId,
            String occurredOn,
            String name,
            List<String> IngredientId
    ) {
        super(aggregateId, eventId, occurredOn);
        this.name = name;
        this.ingredientIds = IngredientId;
    }


    @Override
    public String eventName() {
        return  "plate.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        HashMap<String, Serializable> map = new HashMap<>();
        map.put("name", name);
        map.put("ingredients", (Serializable) ingredientIds);
        return map;
    }

    @Override
    public PlateCreateDomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        String name = (String) body.get("name");
        List<String> ingredientIds = (List<String>) body.get("ingredients");

        return new PlateCreateDomainEvent(
                aggregateId,
                eventId,
                occurredOn,
                name,
                ingredientIds
        );
    }
}
