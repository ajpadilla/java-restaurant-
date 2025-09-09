package restaurant.order.menu.infrastructure.persistence;

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
import restaurant.order.menu.application.find.dto.IngredientResponse;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.menu.domain.PlateRepository;
import restaurant.order.menu.infrastructure.entity.PlateEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaPlateRepository implements PlateRepository {
    private final EntityManager entityManager;

    @Override
    public void save(Plate plate) {

        System.out.println("Guardo el plato");
        PlateEntity plateEntity = PlateEntity.fromDomain(plate);
        this.entityManager.persist(plateEntity);
    }

    @Override
    public Optional<Plate> search(PlateId id) {
        PlateEntity plateEntity = this.entityManager.find(PlateEntity.class, id.getValue());
        return Optional.ofNullable(plateEntity.toDomain());
    }


    @Override
    public Page<PlateResponse> searchAll(int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlateEntity> cq = cb.createQuery(PlateEntity.class);
        Root<PlateEntity> rootEntry = cq.from(PlateEntity.class);
        cq.select(rootEntry);

        Pageable pageable = PageRequest.of(page, size);

        // Query plates with pagination
        TypedQuery<PlateEntity> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<PlateEntity> platesEntity = query.getResultList();

        // Convert entities -> DTOs
        List<PlateResponse> plates = platesEntity.stream()
                .map(p -> new PlateResponse(
                        p.getId(),
                        p.getName(),
                        p.getIngredients().stream()
                                .map(i -> new IngredientResponse(
                                        i.getId(),
                                        i.getName(),
                                        i.getQuantity()
                                ))
                                .toList()
                ))
                .toList();

        // Count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(PlateEntity.class)));
        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(plates, pageable, totalElements);
    }
}
