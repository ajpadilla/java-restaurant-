package restaurant.order.menu.domain;

import restaurant.order.menu.domain.Ingredient;
import restaurant.order.menu.domain.exception.IngredientQuantityMustBePositiveException;
import restaurant.order.menu.domain.exception.InvalidPlateNameException;
import restaurant.order.menu.domain.exception.PlateWithoutIngredientsException;
import restaurant.order.menu.domain.exception.TooManyIngredientsException;
import restaurant.order.shared.domain.AggregateRoot;

import java.io.Serializable;
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
        validateName(name);
        validateIngredients(ingredients);

        Plate plate = new Plate(id, name, ingredients);

        List<String> ingredientIds = ingredients.stream()
                .map(ingredient -> ingredient.getId().getValue())
                .toList();

        plate.record(new PlateCreateDomainEvent(id.getValue(), name.getValue(), ingredientIds));
        return plate;
    }

    private static void validateName(PlateName name) {
        if (name == null || name.getValue().isBlank()) {
            throw new InvalidPlateNameException("Plate name cannot be empty");
        }
    }

    private static void validateIngredients(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new PlateWithoutIngredientsException("A plate must have at least one ingredient");
        }
        if (ingredients.size() > 5) {
            throw new TooManyIngredientsException("A plate cannot have more than 5 ingredients");
        }
        boolean invalidQuantities = ingredients.stream()
                .anyMatch(ingredient -> ingredient.getQuantity().getValue() <= 0);
        if (invalidQuantities) {
            throw new IngredientQuantityMustBePositiveException("All ingredient quantities must be positive");
        }
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
