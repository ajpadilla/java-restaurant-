package restaurant.order.ingredients.application.create;

import restaurant.order.shared.domain.bus.command.Command;

public class CreateIngredientCommand implements Command {
    private final String id;
    private final String name;
    private final Integer quantity;

    public CreateIngredientCommand(String id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
