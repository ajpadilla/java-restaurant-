package restautant.kitchen.ingredient.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
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
import restaurant.store.ingredients.infrastructure.entity.IngredientEntity;

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
    public Optional<Ingredient> findByIdWithLock(IngredientId ingredientId) {
        // Usamos un bloqueo PESSIMISTIC_WRITE para evitar modificaciones concurrentes
        IngredientEntity ingredientEntity = this.entityManager
                .find(IngredientEntity.class, ingredientId.getValue(), LockModeType.PESSIMISTIC_WRITE);

        // Convertir la entidad JPA de nuevo a la clase de dominio
        if (ingredientEntity != null) {
            return Optional.ofNullable(ingredientEntity.toDomain());
        } else {
            throw new EntityNotFoundException("Ingredient with id " + ingredientId.getValue() + " not found");
        }
    }

    @Override
    public Optional<Ingredient> search(IngredientId id) {
        IngredientEntity ingredientEntity = this.entityManager.find(IngredientEntity.class, id.getValue());
        return Optional.ofNullable(ingredientEntity.toDomain());
    }

    @Override
    public Optional<Ingredient> searchByName(String name) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<IngredientEntity> cq = cb.createQuery(IngredientEntity.class);
        Root<IngredientEntity> root = cq.from(IngredientEntity.class);

        // Condición de búsqueda por nombre
        cq.select(root).where(cb.equal(root.get("name"), name));

        TypedQuery<IngredientEntity> query = this.entityManager.createQuery(cq);

        List<IngredientEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        IngredientEntity ingredientEntity = resultList.get(0);
        return Optional.ofNullable(ingredientEntity.toDomain());
    }

    @Override
    public Optional<Ingredient> searchByNameWithLock(String name) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<IngredientEntity> cq = cb.createQuery(IngredientEntity.class);
        Root<IngredientEntity> root = cq.from(IngredientEntity.class);

        // Condición de búsqueda por nombre
        cq.select(root).where(cb.equal(root.get("name"), name));

        TypedQuery<IngredientEntity> query = this.entityManager.createQuery(cq);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE); // Aplicar bloqueo pesimista

        List<IngredientEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        IngredientEntity ingredientEntity = resultList.get(0);
        return Optional.ofNullable(ingredientEntity.toDomain());
    }

    @Override
    public void updateByName(String name, IngredientQuantity quantity) {
        // Buscar la entidad por nombre
        Optional<Ingredient> optionalIngredient = searchByName(name);

        if (optionalIngredient.isPresent()) {
            IngredientEntity ingredientEntity = this.entityManager.find(IngredientEntity.class, optionalIngredient.get().getId().getValue());

            // Actualizamos los campos necesarios
            //ingredientEntity.setName(ingredient.getName().getValue());
            ingredientEntity.setQuantity(quantity.getValue());
            //ingredientEntity.setReserved_quantity(ingredient.getReservedQuantity().getValue());

            // Evitamos que la entidad se duplique en el contexto de la sesión
            if (this.entityManager.contains(ingredientEntity)) {
                // Si la entidad ya está gestionada por la sesión, no es necesario usar merge
                this.entityManager.flush(); // Sincroniza los cambios con la base de datos
            } else {
                // Si no está gestionada, la gestionamos con merge
                this.entityManager.merge(ingredientEntity);
            }
        } else {
            throw new EntityNotFoundException("Ingredient with name " + name + " not found");
        }
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
