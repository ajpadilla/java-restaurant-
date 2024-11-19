package restaurant.order.ingredients.infrastructure.persistence;

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
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.ingredients.domain.IngredientRepository;
import restaurant.order.ingredients.infrastructure.entity.IngredientEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaIngredientRepository implements IngredientRepository {
    private final EntityManager entityManager;
    @Override
    public void save(Ingredient ingredient) {
        IngredientEntity ingredientEntity = IngredientEntity.fromDomain(ingredient);
        this.entityManager.persist(ingredientEntity);
    }

    @Override
    public Optional<Ingredient> search(IngredientId id) {
        IngredientEntity ingredientEntity = this.entityManager.find(IngredientEntity.class, id.getValue());
        return Optional.ofNullable(ingredientEntity.toDomain());
    }

    @Override
    public Page<Ingredient> searchAll(int page, int size) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<IngredientEntity> cq = cb.createQuery(IngredientEntity.class);
        Root<IngredientEntity> rootEntry = cq.from(IngredientEntity.class);
        cq.select(rootEntry);

        // Crear Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Aplicar paginación en la consulta
        TypedQuery<IngredientEntity> query = this.entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Obtener resultados
        List<IngredientEntity> ingredientsEntity = query.getResultList();
        //System.out.println("IngredientsEntity:" + ingredientsEntity.stream().toList());

        // Convertir a dominio
        List<Ingredient> ingredients = ingredientsEntity.stream()
                .map(IngredientEntity::toDomain)
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
