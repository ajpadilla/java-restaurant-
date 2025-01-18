package restautant.kitchen.order.infrastructure.persistence;

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
import restautant.kitchen.ingredient.infrastructure.entity.IngredientEntity;
import restautant.kitchen.order.domain.Order;
import restautant.kitchen.order.domain.OrderId;
import restautant.kitchen.order.domain.OrderRepository;
import restautant.kitchen.order.infrastructure.entity.OrderEntity;
import restautant.kitchen.plate.infrastructure.PlateEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaOrderRepository implements OrderRepository {

    private final EntityManager entityManager;

    @Override
    public void save(Order order) {
        // Convertir el dominio Order a OrderEntity
        OrderEntity orderEntity = OrderEntity.fromDomain(order);

        // Manejar la persistencia de PlateEntity
        List<PlateEntity> plateEntities = orderEntity.getPlates();
        plateEntities.forEach(plateEntity -> {
            if (plateEntity != null) {
                // Verificar si el PlateEntity ya está persistido
                PlateEntity managedPlateEntity = this.entityManager.find(PlateEntity.class, plateEntity.getId());

                if (managedPlateEntity == null) {
                    // Persistir PlateEntity si no está ya guardado
                    this.entityManager.persist(plateEntity);
                } else {
                    // Actualizar la referencia en OrderEntity
                    orderEntity.getPlates().set(orderEntity.getPlates().indexOf(plateEntity), managedPlateEntity);
                }

                // Manejar la persistencia de IngredientEntity dentro del PlateEntity
                List<IngredientEntity> ingredientEntities = plateEntity.getIngredientEntityList();
                ingredientEntities.forEach(ingredientEntity -> {
                    if (ingredientEntity != null) {
                        // Verificar si el IngredientEntity ya está persistido
                        IngredientEntity managedIngredientEntity = this.entityManager.find(IngredientEntity.class, ingredientEntity.getId());

                        if (managedIngredientEntity == null) {
                            // Persistir IngredientEntity si no está ya guardado
                            this.entityManager.persist(ingredientEntity);
                        } else {
                            // Actualizar la referencia en PlateEntity
                            plateEntity.getIngredientEntityList().set(plateEntity.getIngredientEntityList().indexOf(ingredientEntity), managedIngredientEntity);
                        }
                    }
                });
            }
        });

        // Finalmente, persistir el OrderEntity
        this.entityManager.persist(orderEntity);
    }


    @Override
    public Optional<Order> search(OrderId id) {
        OrderEntity orderEntity = this.entityManager.find(OrderEntity.class, id.getValue());
        return Optional.ofNullable(orderEntity.toDomain());
    }

    @Override
    public Page<Order> searchAll(int page, int size) {
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
        List<Order> orders = orderEntity.stream()
                .map(OrderEntity::toDomain)
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
