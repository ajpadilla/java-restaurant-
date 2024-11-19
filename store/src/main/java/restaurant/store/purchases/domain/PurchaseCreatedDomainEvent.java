package restaurant.store.purchases.domain;

import restaurant.store.ingredients.domain.IngredientCreatedDomainEvent;
import restaurant.store.ingredients.domain.IngredientId;
import restaurant.store.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class PurchaseCreatedDomainEvent extends DomainEvent<PurchaseCreatedDomainEvent> {

    private final String description;
    private final Integer quantity;

    private final String ingredientId;


    public PurchaseCreatedDomainEvent(String aggregateId, String description, Integer quantity, String ingredientId) {
        super(aggregateId);
        this.description = description;
        this.quantity = quantity;
        this.ingredientId = ingredientId;
    }

    public PurchaseCreatedDomainEvent(
            String aggregateId,
            String eventId,
            String occurredOn,
            String description,
            int quantity,
            String ingredientId
    ) {
        super(aggregateId, eventId, occurredOn);
        this.description = description;
        this.quantity = quantity;
        this.ingredientId = ingredientId;
    }


    @Override
    public String eventName() {
        return "purchase.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return new HashMap<String, Serializable>() {{
            put("description", description);
            put("quantity", quantity);
            put("ingredient_id", ingredientId);
        }};
    }

    @Override
    public PurchaseCreatedDomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        return null;
    }
}
