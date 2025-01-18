package restautant.kitchen.plate.infrastructure;


import jakarta.persistence.*;
import lombok.*;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredient.infrastructure.entity.IngredientEntity;
import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.plate.domain.PlateId;
import restautant.kitchen.plate.domain.PlateName;

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

   /* @OneToMany(mappedBy = "plate")
    private List<OrderEntity> orders;*/

    @ManyToMany
    @JoinTable(
            name = "ingredient_plate",
            joinColumns = @JoinColumn(name = "plate_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<IngredientEntity> ingredientEntityList;

    public static PlateEntity fromDomain(Plate plate) {
        List<IngredientEntity> ingredientEntities = plate.getIngredients().stream()
                .map(IngredientEntity::fromDomain)
                .toList();
        return new PlateEntity(plate.getId().getValue(), plate.getName().getValue(), ingredientEntities);
    }

    public Plate toDomain() {
        List<Ingredient> ingredients = this.ingredientEntityList.stream()
                .map(IngredientEntity::toDomain)
                .toList();
        return new Plate(new PlateId(this.id), new PlateName(this.name), ingredients);
    }
}
