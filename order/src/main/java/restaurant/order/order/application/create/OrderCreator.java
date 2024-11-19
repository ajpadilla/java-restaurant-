package restaurant.order.order.application.create;

import org.springframework.stereotype.Service;
import restaurant.order.kafka.KafkaOrderJsonService;
import restaurant.order.order.domain.CastOrderToJsonService;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderId;
import restaurant.order.order.domain.OrderRepository;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateService;

@Service
public class OrderCreator {

    private final OrderRepository repository;

    private final PlateService service;

    private final KafkaOrderJsonService orderJsonService;


    public OrderCreator(OrderRepository repository, PlateService service, KafkaOrderJsonService orderJsonService) {
        this.repository = repository;
        this.service = service;
        this.orderJsonService = orderJsonService;
    }

    public void create(OrderId id, PlateId plateId) {
        Plate plate = this.service.findAnExistingPlate(plateId);
        Order order = Order.create(id, plate);
        this.orderJsonService.send(new CastOrderToJsonService(order).createMap());
        this.repository.save(order);
    }
}
