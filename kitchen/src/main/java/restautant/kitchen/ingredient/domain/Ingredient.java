package restautant.kitchen.ingredient.domain;



import restautant.kitchen.shared.domain.AggregateRoot;

import java.util.Objects;

public class Ingredient extends AggregateRoot {
    private final IngredientId id;
    private final IngredientName name;

    private final IngredientQuantity quantity;


    public Ingredient(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }


    public static Ingredient create(IngredientId id, IngredientName name, IngredientQuantity quantity) {
        Ingredient ingredient = new Ingredient(id, name, quantity);
        return ingredient;
    }

    public Ingredient() {
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
        Ingredient that = (Ingredient) o;
        return Objects.equals(this.id.getValue(), that.id.getValue()) && Objects.equals(this.name.getValue(), that.name.getValue()) && Objects.equals(this.quantity.getValue(), that.quantity.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + this.id.getValue() +
                ", name=" + this.name.getValue() +
                ", quantity=" + this.quantity.getValue() +
                '}';
    }
}
