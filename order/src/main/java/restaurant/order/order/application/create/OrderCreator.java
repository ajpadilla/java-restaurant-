package restaurant.order.order.application.create;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import restaurant.order.kafka.KafkaOrderJsonService;
import restaurant.order.order.domain.CastOrderToJsonService;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderId;
import restaurant.order.order.domain.OrderRepository;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.menu.domain.PlateService;
import restaurant.order.shared.domain.bus.event.EventBus;

import java.util.List;

@Service
public class OrderCreator {

    private final OrderRepository repository;

    private final PlateService service;

    private final EventBus eventBus;
    private final KafkaOrderJsonService orderJsonService;


    public OrderCreator(OrderRepository repository, PlateService service, KafkaOrderJsonService orderJsonService, @Qualifier("postgreSqlEventBus") EventBus eventBus) {
        this.repository = repository;
        this.service = service;
        this.orderJsonService = orderJsonService;
        this.eventBus = eventBus;
    }

    public void create(OrderId id, List<PlateId> plateIds) {
        List<Plate> plates = plateIds
                .stream()
                .map(this.service::findAnExistingPlate)
                .toList();

        Order order = Order.create(id, plates);
        this.orderJsonService.send(new CastOrderToJsonService(order).createMap());
        this.repository.save(order);
        this.eventBus.publish(order.pullDomainEvents());
    }
}