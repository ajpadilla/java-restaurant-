package restaurant.store.purchases.infrastructure.persistence;

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
import restaurant.store.ingredients.domain.Ingredient;
import restaurant.store.ingredients.infrastructure.entity.IngredientEntity;
import restaurant.store.purchases.domain.Purchase;
import restaurant.store.purchases.domain.PurchaseId;
import restaurant.store.purchases.domain.PurchaseRepository;
import restaurant.store.purchases.infrastructure.entity.PurchaseEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaPurchaseRepository implements PurchaseRepository {

    private final EntityManager entityManager;
    @Override
    public void save(Purchase purchase) {
        PurchaseEntity purchaseEntity = PurchaseEntity.fromDomain(purchase);
        this.entityManager.persist(purchaseEntity);
    }

    @Override
    public Optional<Purchase> search(PurchaseId id) {
        PurchaseEntity purchaseEntity = this.entityManager.find(PurchaseEntity.class, id.getValue());
        return Optional.ofNullable(purchaseEntity.toDomain());
    }

    @Override
    public Optional<Purchase> searchByName(String name) {
        return Optional.empty();
    }

    @Override
    public Page<Purchase> searchAll(int page, int size) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<PurchaseEntity> cq = cb.createQuery(PurchaseEntity.class);
        Root<PurchaseEntity> rootEntry = cq.from(PurchaseEntity.class);
        cq.select(rootEntry);

        // Crear Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Aplicar paginación en la consulta
        TypedQuery<PurchaseEntity> query = this.entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Obtener resultados
        List<PurchaseEntity> ingredientsEntity = query.getResultList();
        //System.out.println("IngredientsEntity:" + ingredientsEntity.stream().toList());

        // Convertir a dominio
        List<Purchase> ingredients = ingredientsEntity.stream()
                .map(PurchaseEntity::toDomain)
                .toList();

        //System.out.println("ingredients:" + ingredients);

        // Contar total de elementos
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(IngredientEntity.class)));
        Long totalElements = this.entityManager.createQuery(countQuery).getSingleResult();


        // Devolver resultados paginados
        return new PageImpl<>(ingredients, pageable, totalElements);
    }
}
