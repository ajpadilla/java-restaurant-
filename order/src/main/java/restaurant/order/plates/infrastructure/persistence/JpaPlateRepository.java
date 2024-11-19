package restaurant.order.plates.infrastructure.persistence;

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
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateRepository;
import restaurant.order.plates.infrastructure.entity.PlateEntity;

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
    public Page<Plate> searchAll(int page, int size) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<PlateEntity> cq = cb.createQuery(PlateEntity.class);
        Root<PlateEntity> rootEntry = cq.from(PlateEntity.class);
        cq.select(rootEntry);

        // Crear Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Aplicar paginación en la consulta
        TypedQuery<PlateEntity> query = this.entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Obtener resultados
        List<PlateEntity> platesEntity = query.getResultList();
        //System.out.println("platesEntity:" + platesEntity.stream().toList());

        // Convertir a dominio
        List<Plate> plates = platesEntity.stream()
                .map(PlateEntity::toDomain)
                .toList();

        //System.out.println("plates:" + plates);

        // Contar total de elementos
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(PlateEntity.class)));
        Long totalElements = this.entityManager.createQuery(countQuery).getSingleResult();


        // Devolver resultados paginados
        return new PageImpl<>(plates, pageable, totalElements);
    }
}
