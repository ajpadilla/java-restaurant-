package restaurant.order.menu.infrastructure.entity;


import jakarta.persistence.*;
import lombok.*;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.infrastructure.entity.IngredientEntity;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateName;

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
