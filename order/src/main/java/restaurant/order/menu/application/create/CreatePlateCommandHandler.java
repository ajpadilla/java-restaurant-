package restaurant.order.menu.application.create;

import org.springframework.stereotype.Service;
import restaurant.order.menu.domain.*;
import restaurant.order.shared.cache.Cache;
import restaurant.order.shared.domain.bus.command.CommandHandler;

import java.util.List;

@Service
public class CreatePlateCommandHandler implements CommandHandler<CreatePlateCommand> {

    private final PlateCreator creator;

    private final Cache cache;

    public CreatePlateCommandHandler(PlateCreator creator, Cache cache) {
        this.creator = creator;
        this.cache = cache;
    }

    @Override
    public void handle(CreatePlateCommand command) {
        PlateId id = new PlateId(command.getId());
        PlateName name = new PlateName(command.getName());
        // Map each incoming ingredient command to a domain Ingredient object
        List<Ingredient> ingredients = command.getIngredients().stream()
                .map(plateIngredientCommand -> new Ingredient(
                        new IngredientId(plateIngredientCommand.getIngredientId()),
                        new IngredientName(plateIngredientCommand.getIngredientName()),
                        new IngredientQuantity(plateIngredientCommand.getRequiredQuantity())
                ))
                .toList();

        this.creator.create(id, name, ingredients);
        Plate plate = new Plate(id, name, ingredients);

        // Invalidate cache
        cache.evict("plate:" + plate.getId().getValue());
        cache.evictByPrefix("plates:"); // paginated lists
    }
}
