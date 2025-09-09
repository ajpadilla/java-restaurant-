package restaurant.order.order.application.create;

import org.springframework.stereotype.Service;
import restaurant.order.order.domain.OrderId;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.shared.domain.bus.command.CommandHandler;

import java.util.List;

@Service
public class CreateOrderCommandHandler implements CommandHandler<CreateOrderCommand> {

    private final OrderCreator creator;

    public CreateOrderCommandHandler(OrderCreator creator) {
        this.creator = creator;
    }

    @Override
    public void handle(CreateOrderCommand command) {
        OrderId id = new OrderId(command.getId());
        List<PlateId> plateIds = command.getPlateIds()
                .stream()
                .map(PlateId::new)
                .toList();
        this.creator.create(id, plateIds);
    }
}
