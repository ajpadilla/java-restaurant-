package restaurant.order.menu.infrastructure.entity;


import jakarta.persistence.*;
import lombok.*;
import restaurant.order.menu.domain.Ingredient;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.menu.domain.PlateName;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlateEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "plate_ingredients", joinColumns = @JoinColumn(name = "plate_id"))
    private List<IngredientEntityEmbeddable> ingredients;

    public static PlateEntity fromDomain(Plate plate) {
        List<IngredientEntityEmbeddable> ingredientEntities = plate.getIngredients().stream()
                .map(IngredientEntityEmbeddable::fromDomain)
                .toList();
        return new PlateEntity(plate.getId().getValue(), plate.getName().getValue(), ingredientEntities);
    }

    public Plate toDomain() {
        List<Ingredient> ingredients = this.ingredients.stream()
                .map(IngredientEntityEmbeddable::toDomain)
                .toList();
        return new Plate(new PlateId(this.id), new PlateName(this.name), ingredients);
    }
}
