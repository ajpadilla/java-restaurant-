package restaurant.order.order.application.create;

import restaurant.order.shared.domain.bus.command.Command;

import java.util.List;

public class CreateOrderCommand implements Command {

    private final String id;
    private final List<String> plateIds;

    public CreateOrderCommand(String id, List<String> plateId) {

        this.id = id;
        this.plateIds = plateId;
    }

    public String getId() {
        return id;
    }

    public List<String> getPlateIds() {
        return plateIds;
    }

}
