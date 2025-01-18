package restaurant.store.purchases.domain;

import restaurant.store.ingredients.domain.Ingredient;
import restaurant.store.shared.domain.AggregateRoot;

public class Purchase extends AggregateRoot {

    private PurchaseId id;

    private PurchaseDescription description;

    private PurchaseQuantity quantity;

    public Purchase(PurchaseId id, PurchaseDescription description, PurchaseQuantity quantity) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
    }

    public static Purchase create(PurchaseId id, PurchaseDescription description, PurchaseQuantity quantity) {
        Purchase purchase = new Purchase(id, description, quantity);
        purchase.record(new PurchaseCreatedDomainEvent(id.getValue(), description.getValue(), quantity.getValue()));
        return purchase;
    }

    public PurchaseId getId() {
        return id;
    }

    public PurchaseDescription getDescription() {
        return description;
    }

    public PurchaseQuantity getQuantity() {
        return quantity;
    }

}
