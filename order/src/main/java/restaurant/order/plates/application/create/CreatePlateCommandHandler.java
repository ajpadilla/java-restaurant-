package restaurant.order.plates.application.create;

import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateName;
import restaurant.order.shared.domain.bus.command.CommandHandler;

import java.util.List;

@Service
public class CreatePlateCommandHandler implements CommandHandler<CreatePlateCommand> {

    private final PlateCreator creator;

    public CreatePlateCommandHandler(PlateCreator creator) {
        this.creator = creator;
    }

    @Override
    public void handle(CreatePlateCommand command) {
        PlateId id = new PlateId(command.getId());
        PlateName name = new PlateName(command.getName());
        List<IngredientId> ingredientsIds = command.getIngredientsIds().stream()
                .map(IngredientId::new)
                .toList();
        this.creator.create(id, name, ingredientsIds);
    }
}
