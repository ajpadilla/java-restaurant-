package restaurant.store.purchases.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import restaurant.store.purchases.domain.Purchase;
import restaurant.store.purchases.domain.PurchaseDescription;
import restaurant.store.purchases.domain.PurchaseId;
import restaurant.store.purchases.domain.PurchaseQuantity;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    public static PurchaseEntity fromDomain(Purchase purchase) {
        return PurchaseEntity
                .builder()
                .id(purchase.getId().getValue())
                .description(purchase.getDescription().getValue())
                .quantity(purchase.getQuantity().getValue())
                .build();
    }

    public Purchase toDomain() {
        return new Purchase(
                new PurchaseId(this.id),
                new PurchaseDescription(this.description),
                new PurchaseQuantity(this.quantity)
        );
    }

}
