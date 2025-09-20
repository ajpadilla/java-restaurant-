package restautant.kitchen.shared.infrastructure.feignclient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import restautant.kitchen.order.infrastructure.controller.CreateOrderRequest;

import java.io.Serializable;
import java.util.HashMap;

@FeignClient(name = "store-service", url = "${app.store-url}")
public interface StoreClient {
    @PostMapping
    @CircuitBreaker(name = "storeClient", fallbackMethod = "fallbackSave")
    ResponseEntity<HashMap<String, Serializable>> save(@RequestBody CreateOrderRequest request);

    default ResponseEntity<HashMap<String, Serializable>> fallbackSave(CreateOrderRequest request, Throwable throwable) {
        System.err.println("Fallback: Store service unavailable. Error: " + throwable.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new HashMap<>());
    }
}
