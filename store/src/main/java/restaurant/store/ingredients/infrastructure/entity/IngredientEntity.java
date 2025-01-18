package restaurant.store.ingredients.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import restaurant.store.ingredients.domain.*;
import restaurant.store.purchases.infrastructure.entity.PurchaseEntity;

import java.util.List;

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

    @Column(nullable = false)
    private Integer reserved_quantity;

    @Version
    private Integer version;

    public static IngredientEntity fromDomain(Ingredient ingredient) {
        return IngredientEntity
                .builder()
                .id(ingredient.getId().getValue())
                .name(ingredient.getName().getValue())
                .quantity(ingredient.getQuantity().getValue())
                .reserved_quantity(ingredient.getReservedQuantity().getValue())
                .build();
    }

    public Ingredient toDomain() {
        return new Ingredient(
                new IngredientId(this.id),
                new IngredientName(this.name),
                new IngredientQuantity(this.quantity),
                new IngredientReservedQuantity(this.reserved_quantity),
                new IngredientVersion(this.version)
        );
    }
}
