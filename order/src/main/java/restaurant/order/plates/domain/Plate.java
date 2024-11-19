package restaurant.order.plates.domain;

import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.shared.domain.AggregateRoot;

import java.util.List;
import java.util.Objects;

public class Plate extends AggregateRoot {

    PlateId id;

    PlateName name;

    List<Ingredient> ingredients;

    public Plate(PlateId id, PlateName name, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Plate() {
        this.id = null;
        this.name = null;
        this.ingredients = null;
    }

    public static Plate create(PlateId id, PlateName name, List<Ingredient> ingredients) {
        Plate plate = new Plate(id, name, ingredients);
        plate.record(new PlateCreateDomainEvent(id.getValue(), name.getValue()));
        return plate;
    }

    public PlateId getId() {
        return this.id;
    }

    public PlateName getName() {
        return this.name;
    }
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Plate that = (Plate) obj;
        return Objects.equals(this.id.getValue(), that.id.getValue()) && Objects.equals(this.name.getValue(), that.name.getValue());

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredients);
    }

    @Override
    public String toString() {
        return "Plate{" +
                "id=" + this.id.getValue() +
                ", name=" + this.name.getValue() +
                ", ingredients=" + this.ingredients +
                '}';
    }
}
