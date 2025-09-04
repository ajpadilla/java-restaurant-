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

import java.util.List;
import java.util.stream.Collectors;

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

    public void create(OrderId id, List<PlateId> plateIds) {
        List<Plate> plates = plateIds
                .stream()
                .map(this.service::findAnExistingPlate)
                .toList();

        Order order = Order.create(id, plates);
        this.orderJsonService.send(new CastOrderToJsonService(order).createMap());
        this.repository.save(order);
    }
}