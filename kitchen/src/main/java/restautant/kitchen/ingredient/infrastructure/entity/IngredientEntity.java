package restautant.kitchen.ingredient.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import restautant.kitchen.ingredient.domain.Ingredient;
import restautant.kitchen.ingredient.domain.IngredientId;
import restautant.kitchen.ingredient.domain.IngredientName;
import restautant.kitchen.ingredient.domain.IngredientQuantity;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IngredientEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    public static IngredientEntity fromDomain(Ingredient ingredient) {
        return IngredientEntity
                .builder()
                .id(ingredient.getId().getValue())
                .name(ingredient.getName().getValue())
                .quantity(ingredient.getQuantity().getValue())
                .build();
    }

    public Ingredient toDomain() {
        return new Ingredient(
                new IngredientId(this.id),
                new IngredientName(this.name),
                new IngredientQuantity(this.quantity)
        );
    }
}
