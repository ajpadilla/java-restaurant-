package restautant.kitchen.order.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.order.domain.OrderId;
import restautant.kitchen.order.domain.OrderStatus;
import restautant.kitchen.plate.domain.Plate;
import restautant.kitchen.plate.infrastructure.PlateEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToMany
    @JoinTable(
            name = "order_plate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "plate_id")
    )
    private List<PlateEntity> plates;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public static OrderEntity fromDomain(Order order) {
        List <PlateEntity> plateEntityFromDomain = order.getPlates()
                .stream()
                .map(PlateEntity::fromDomain)
                .collect(Collectors.toList());
        return new OrderEntity(order.getId().getValue(), plateEntityFromDomain, order.getStatus());
    }

    public Order toDomain() {
        List<Plate> plates = this.plates
                .stream()
                .map(PlateEntity::toDomain)
                .toList();

        return new Order(new OrderId(this.id), plates, status);
    }

}
