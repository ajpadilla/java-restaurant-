package restaurant.order.order.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderId;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.infrastructure.entity.PlateEntity;

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

    @ManyToOne(fetch = FetchType.LAZY) // Considera usar LAZY para evitar carga innecesaria
    @JoinColumn(name = "plate_id") // Asegúrate de que este campo no sea nulo
    private PlateEntity plate;

    public static OrderEntity fromDomain(Order order) {
        Plate plateDomain = order.getPlate();
        return new OrderEntity(order.getId().getValue(), PlateEntity.fromDomain(plateDomain));
    }

    public Order toDomain() {
        Plate plateDomain = this.plate.toDomain();
        return new Order(new OrderId(this.id), plateDomain);
    }

}
