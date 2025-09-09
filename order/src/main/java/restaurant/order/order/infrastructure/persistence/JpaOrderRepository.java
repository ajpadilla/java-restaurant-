package restaurant.order.order.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.order.application.find.dto.PlateResponse;
import restaurant.order.order.application.find.dto.OrderResponse;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderId;
import restaurant.order.order.domain.OrderRepository;
import restaurant.order.order.infrastructure.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaOrderRepository implements OrderRepository {

    private final EntityManager entityManager;

    @Override
    public void save(Order order) {
        OrderEntity orderEntity = OrderEntity.fromDomain(order);
        this.entityManager.persist(orderEntity);
    }

    @Override
    public Optional<Order> search(OrderId id) {
        OrderEntity orderEntity = this.entityManager.find(OrderEntity.class, id.getValue());
        return Optional.ofNullable(orderEntity.toDomain());
    }

    @Override
    public Page<OrderResponse> searchAll(int page, int size) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);
        Root<OrderEntity> rootEntry = cq.from(OrderEntity.class);
        cq.select(rootEntry);

        // Crear Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Aplicar paginación en la consulta
        TypedQuery<OrderEntity> query = this.entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Obtener resultados
        List<OrderEntity> orderEntity = query.getResultList();
        //System.out.println("orderEntity:" + orderEntity.stream().toList());

        // Convertir a dominio
        /**List<Order> orders = orderEntity.stream()
                .map(OrderEntity::toDomain)
                .toList();*/

        // Convert entities -> DTOs
        List<OrderResponse> orders = orderEntity.stream()
                .map(o -> new OrderResponse(
                        o.getId(),
                        o.getPlates().stream()
                                .map(p -> new PlateResponse(
                                        p.getId()
                                ))
                                .toList()
                ))
                .toList();

        //System.out.println("orders:" + orders);

        // Contar total de elementos
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(OrderEntity.class)));
        Long totalElements = this.entityManager.createQuery(countQuery).getSingleResult();


        // Devolver resultados paginados
        return new PageImpl<>(orders, pageable, totalElements);
    }
}
