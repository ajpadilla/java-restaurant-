package restaurant.order.order.application.create;

import org.springframework.stereotype.Service;
import restaurant.order.order.domain.OrderId;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.shared.domain.bus.command.CommandHandler;

@Service
public class CreateOrderCommandHandler implements CommandHandler<CreateOrderCommand> {

    private final OrderCreator creator;

    public CreateOrderCommandHandler(OrderCreator creator) {
        this.creator = creator;
    }

    @Override
    public void handle(CreateOrderCommand command) {
        OrderId id = new OrderId(command.getId());
        PlateId plateId = new PlateId(command.getPlateId());
        this.creator.create(id, plateId);
    }
}
