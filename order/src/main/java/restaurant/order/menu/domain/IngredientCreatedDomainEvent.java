package restaurant.order.menu.domain;

import restaurant.order.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class IngredientCreatedDomainEvent extends DomainEvent<IngredientCreatedDomainEvent> {
    private final String name;
    private final Integer quantity;

    public IngredientCreatedDomainEvent() {
        super(null);
        this.name = null;
        this.quantity = 0;
    }

    public IngredientCreatedDomainEvent(String aggregateId, String name, Integer quantity) {
        super(aggregateId);
        this.name = name;
        this.quantity = quantity;
    }

    public IngredientCreatedDomainEvent(
            String aggregateId,
            String eventId,
            String occurredOn,
            String name,
            int quantity
    ) {
        super(aggregateId, eventId, occurredOn);
        this.name = name;
        this.quantity = quantity;
    }


    @Override
    public String eventName() {
        return "ingredient.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return new HashMap<String, Serializable>() {{
            put("name", name);
            put("quantity", quantity);
        }};
    }

    @Override
    public IngredientCreatedDomainEvent fromPrimitives(
            String aggregateId,
            HashMap<String, Serializable> body,
            String eventId,
            String occurredOn
    ) {
        return new IngredientCreatedDomainEvent(
                aggregateId,
                eventId,
                occurredOn,
                (String) body.get("name"),
                (Integer) body.get("quantity")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IngredientCreatedDomainEvent that = (IngredientCreatedDomainEvent) o;
        assert name != null;
        return name.equals(that.name) &&
                quantity == (that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity);
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
