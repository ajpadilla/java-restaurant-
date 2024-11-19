package restaurant.order.plates.application.create;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.ingredients.domain.IngredientService;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateName;
import restaurant.order.plates.domain.PlateRepository;
import restaurant.order.shared.domain.bus.event.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlateCreator {
    private final PlateRepository repository;
    private IngredientService ingredientService = null;
    List<Ingredient> ingredients;

    private final EventBus eventBus;

    public PlateCreator(PlateRepository repository, IngredientService ingredientService, @Qualifier("postgreSqlEventBus") EventBus eventBus) {
        this.repository = repository;
        this.ingredientService = ingredientService;
        this.ingredients = new ArrayList<>();
        this.eventBus = eventBus;
    }

    private void verifyingIngredientsBeforeToAddToPlate(List<IngredientId> ingredientIds) {
       this.ingredients = ingredientIds.stream()
               .map(this.ingredientService::findAnExistingIngredient)
               .collect(Collectors.toList());
    }

    public void create(PlateId id, PlateName name, List<IngredientId> ingredientIds) {
        this.verifyingIngredientsBeforeToAddToPlate(ingredientIds);
        Plate plate = Plate.create(id, name, this.ingredients);
        this.repository.save(plate);
        this.eventBus.publish(plate.pullDomainEvents());
    }
}
