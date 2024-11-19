package restaurant.store.ingredients.domain;



import restaurant.store.shared.domain.AggregateRoot;

import java.util.Objects;

public class Purchase extends AggregateRoot {
    private final IngredientId id;
    private final IngredientName name;

    private final IngredientQuantity quantity;


    public Purchase(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }


    public static Purchase create(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        Purchase ingredient = new Purchase(id, name, quantity);
        ingredient.record(new IngredientCreatedDomainEvent(id.getValue(), name.getValue(), quantity.getValue()));
        return ingredient;
    }

    public Purchase() {
        this.id = null;
        this.name = null;
        this.quantity = null;
    }

    public IngredientId getId() {
        return id;
    }

    public IngredientName getName() {
        return name;
    }

    public IngredientQuantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase that = (Purchase) o;
        return Objects.equals(this.id.getValue(), that.id.getValue()) && Objects.equals(this.name.getValue(), that.name.getValue()) && Objects.equals(this.quantity.getValue(), that.quantity.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        assert this.id != null;
        assert this.name != null;
        assert this.quantity != null;
        return "Ingredient{" +
                "id=" + this.id.getValue() +
                ", name=" + this.name.getValue() +
                ", quantity=" + this.quantity.getValue() +
                '}';
    }
}
