package restaurant.store.ingredients.domain;

import restaurant.store.shared.domain.AggregateRoot;

import java.util.Objects;

public class Ingredient extends AggregateRoot {
    private final IngredientId id;
    private final IngredientName name;

    private IngredientQuantity quantity;

    private IngredientReservedQuantity reservedQuantity;

    private final IngredientVersion version;


    public Ingredient(IngredientId id, IngredientName name, IngredientQuantity quantity, IngredientReservedQuantity reservedQuantity, IngredientVersion version) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.version = version;
    }


    public static Ingredient create(IngredientId id, IngredientName name, IngredientQuantity quantity, IngredientReservedQuantity reservedQuantity, IngredientVersion version) {
        Ingredient ingredient = new Ingredient(id, name, quantity, reservedQuantity, version);
        ingredient.record(new IngredientCreatedDomainEvent(id.getValue(), name.getValue(), quantity.getValue()));
        return ingredient;
    }

    public Ingredient(IngredientReservedQuantity reservedQuantity, IngredientVersion version) {
        this.reservedQuantity = reservedQuantity;
        this.version = version;
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

    public IngredientReservedQuantity getReservedQuantity() {
        return reservedQuantity;
    }

    public IngredientVersion getVersion() {
        return version;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(quantity, that.quantity) && Objects.equals(reservedQuantity, that.reservedQuantity) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, reservedQuantity, version);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name=" + name +
                ", quantity=" + quantity +
                ", reservedQuantity=" + reservedQuantity +
                ", version=" + version +
                '}';
    }

    public void setReservedQuantity(IngredientReservedQuantity reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public void setQuantity(IngredientQuantity quantity) {
        this.quantity = quantity;
    }
}
