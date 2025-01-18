package restautant.kitchen.order.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderId;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.infrastructure.entity.PlateEntity;

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

    public static OrderEntity fromDomain(Order order) {
        List <PlateEntity> plateEntityFromDomain = order.getPlates()
                .stream()
                .map(PlateEntity::fromDomain)
                .collect(Collectors.toList());
        return new OrderEntity(order.getId().getValue(), plateEntityFromDomain);
    }

    public Order toDomain() {
        List<Plate> plates = this.plates
                .stream()
                .map(PlateEntity::toDomain)
                .toList();

        return new Order(new OrderId(this.id), plates);
    }

}
