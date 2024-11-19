package restaurant.order.order.application.create;

import restaurant.order.shared.domain.bus.command.Command;

public class CreateOrderCommand implements Command {

    private final String id;
    private final String plateId;

    public CreateOrderCommand(String id, String plateId) {

        this.id = id;
        this.plateId = plateId;
    }


    public String getId() {
        return id;
    }

    public String getPlateId() {

        return plateId;
    }

}
