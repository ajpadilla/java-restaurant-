package restaurant.order.menu.infrastructure.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import restaurant.order.menu.domain.Ingredient;
import restaurant.order.menu.domain.IngredientId;
import restaurant.order.menu.domain.IngredientName;
import restaurant.order.menu.domain.IngredientQuantity;

@Embeddable
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientEntityEmbeddable {
    private String id;
    private String name;
    private Integer quantity;

    public static IngredientEntityEmbeddable fromDomain(Ingredient ingredient) {
        return IngredientEntityEmbeddable
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
