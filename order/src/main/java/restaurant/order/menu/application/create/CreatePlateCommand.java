package restaurant.order.menu.application.create;

import restaurant.order.shared.domain.bus.command.Command;

import java.util.List;

public class CreatePlateCommand implements Command {

    private final String id;
    private final String name;

    private final List<String> ingredientsIds;

    public CreatePlateCommand(String id, String name, List<String> ingredientsIds) {
        this.id = id;
        this.name = name;
        this.ingredientsIds = ingredientsIds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public List<String> getIngredientsIds() {
        return ingredientsIds;
    }
}
