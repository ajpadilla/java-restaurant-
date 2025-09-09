package restaurant.order.menu.application.create;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import restaurant.order.menu.domain.*;
import restaurant.order.menu.infrastructure.api.InMemoryInventoryClientService;
import restaurant.order.shared.domain.bus.event.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlateCreator {
    private final PlateRepository repository;

    private final EventBus eventBus;

    private final InMemoryInventoryClientService inventoryClientService;

    public PlateCreator(PlateRepository repository, @Qualifier("postgreSqlEventBus") EventBus eventBus, InMemoryInventoryClientService inventoryClientService) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.inventoryClientService = inventoryClientService;
    }


    public void create(PlateId id, PlateName name, List<Ingredient> ingredients) {
        // Validate ingredients against inventory
        List<Ingredient> validIngredients = ingredients.stream()
                .map(ingredient -> inventoryClientService.findIngredient(ingredient.getId()))
                .toList();

        Plate plate = Plate.create(id, name, ingredients);
        this.repository.save(plate);
        this.eventBus.publish(plate.pullDomainEvents());
    }
}
