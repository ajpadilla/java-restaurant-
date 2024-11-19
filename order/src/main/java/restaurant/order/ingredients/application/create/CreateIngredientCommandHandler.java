package restaurant.order.ingredients.application.create;

import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.ingredients.domain.IngredientName;
import restaurant.order.ingredients.domain.IngredientQuantity;
import restaurant.order.shared.domain.bus.command.CommandHandler;

@Service
public class CreateIngredientCommandHandler implements CommandHandler<CreateIngredientCommand> {
    private final IngredientCreator creator;

    public CreateIngredientCommandHandler(IngredientCreator creator) {
        this.creator = creator;
    }

    @Override
    public void handle(CreateIngredientCommand command) {
        IngredientId id = new IngredientId(command.getId());
        IngredientName name = new IngredientName(command.getName());
        IngredientQuantity quantity = new IngredientQuantity(command.getQuantity());

        creator.create(id, name, quantity);
        System.out.println("Ingredient Created");
    }
}
