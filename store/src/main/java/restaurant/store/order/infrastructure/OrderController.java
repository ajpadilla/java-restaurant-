package restaurant.store.order.infrastructure;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.store.ingredients.application.AvailabilityIngredientService;
import restaurant.store.order.infrastructure.request.CreateOrderRequest;

import java.io.Serializable;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private final AvailabilityIngredientService ingredientService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HashMap<String, Serializable>> save(@RequestBody CreateOrderRequest request) {
        logger.info("Received CreateOrderRequest: {}", request);
        HashMap<String, Serializable> orderMap = this.ingredientService.processOrder(request);
        return ResponseEntity.ok(orderMap);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health check passed - valid role!");
    }
}
