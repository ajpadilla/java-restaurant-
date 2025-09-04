package restaurant.order.order.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.order.order.application.create.CreateOrderCommand;
import restaurant.order.order.application.find.FindOrdersQuery;
import restaurant.order.order.application.find.ListOfOrdersResponse;
import restaurant.order.order.domain.Order;
import restaurant.order.order.infrastructure.controller.requests.CreateOrderRequest;
import restaurant.order.shared.Infrastructure.ApiController;
import restaurant.order.shared.domain.bus.command.CommandBus;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;
import restaurant.order.shared.domain.bus.query.QueryBus;
import restaurant.order.shared.domain.bus.query.QueryHandlerExecutionError;

import io.micrometer.core.annotation.Timed;


@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController extends ApiController {

    @Autowired
    private final CommandBus commandBus;

    @Autowired
    private final QueryBus queryBus;


    public OrdersController(QueryBus queryBus, CommandBus commandBus) {
        super(queryBus, commandBus);
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @Timed(value = "orders.health", description = "Time taken to check health", histogram = true)
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        String threadName = Thread.currentThread().getName();
        System.out.println("Handling health check on thread: " + threadName);

        long start = System.currentTimeMillis();
        long sum = 0;

        // Busy loop for ~5 seconds to simulate CPU load
        while (System.currentTimeMillis() - start < 5000) {
            sum += (long) Math.sqrt(Math.random() * 1000);
        }

        return ResponseEntity.ok("Thread: " + threadName + " handled the request with sum: " + sum);
    }



    @Timed(value = "orders.index", description = "Time taken to list orders")
    @GetMapping("/index")
    public ResponseEntity<Page<Order>> index(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) throws QueryHandlerExecutionError {
        ListOfOrdersResponse response = this.queryBus.ask(new FindOrdersQuery(page, size));
        return ResponseEntity.ok(response.response());
    }

    @Timed(value = "orders.create", description = "Time taken to create an order")
    @PostMapping("/create")
    public ResponseEntity<String> register(@RequestBody CreateOrderRequest request) throws CommandNotRegisteredError {
        this.dispatch(new CreateOrderCommand(request.getId(), request.getPlateIds()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
