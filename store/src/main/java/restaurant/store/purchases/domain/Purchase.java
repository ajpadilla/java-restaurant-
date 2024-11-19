package restaurant.store.purchases.domain;

import restaurant.store.ingredients.domain.Ingredient;
import restaurant.store.shared.domain.AggregateRoot;

public class Purchase extends AggregateRoot {

    private PurchaseId id;

    private PurchaseDescription description;

    private PurchaseQuantity quantity;

    private Ingredient ingredient;

    public Purchase(PurchaseId id, PurchaseDescription description, PurchaseQuantity quantity, Ingredient ingredient) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.ingredient = ingredient;
    }

    public static Purchase create(PurchaseId id, PurchaseDescription description, PurchaseQuantity quantity, Ingredient ingredient) {
        Purchase purchase = new Purchase(id, description, quantity, ingredient);
        purchase.record(new PurchaseCreatedDomainEvent(id.getValue(), description.getValue(), quantity.getValue(), ingredient.getId().getValue()));
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

    public Ingredient getIngredient() {
        return ingredient;
    }
}
