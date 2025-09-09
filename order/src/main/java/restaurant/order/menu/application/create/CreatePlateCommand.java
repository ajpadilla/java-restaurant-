package restaurant.order.menu.application.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import restaurant.order.shared.domain.bus.command.Command;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlateCommand implements Command {

    private String id;
    private String name;

    private List<PlateIngredientCommand> ingredients; // full ingredient info

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlateIngredientCommand {
        private String ingredientId;
        private String ingredientName;
        private int requiredQuantity;
    }
}