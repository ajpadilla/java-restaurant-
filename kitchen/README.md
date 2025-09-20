# Kitchen Microservice

Este microservicio es parte de un sistema para gestionar la cocina en un restaurante. Se encarga de procesar pedidos, reservar ingredientes y realizar validaciones. A continuación, se detalla la estructura del proyecto.

## Simplified Workflow

```plaintext
Client --> Kafka(orderCreatedTopic) --> KitchenService.consume()
       --> OrderService.create()
       --> Redis: Save order saga
       --> StoreClient.save(createOrderRequest)
       --> OrderValidationService validates ingredients
       --> If complete: Redis cleanup & order confirmation
       --> If failed: send to pendingOrdersTopic for retry
```

# Key Responsibilities

- **`Consume Orders:`**: Listens to the orderCreatedTopic in Kafka and creates order objects in the system.

- **`Validate and Map Orders:`**: Uses OrderValidationService and OrderMapper to ensure order integrity and prepare requests for the store.

- **`Saga Management:`**: Stores intermediate order state in Redis to manage long-running processes reliably.

- **`External Integration:`**: Sends orders to external store services and handles responses.

- **`Retry Mechanism:`**: Failed orders are sent to pendingOrdersTopic for retry processing.

- **`Event-Driven Flow:`**: Listens to ingredient reservation events (ingredientReservedTopic) and completes orders when all ingredients are available.

- **`Order Completion:`**: Confirms and cleans up orders in Redis when all items are successfully processed.

## Architecture Highlights

- **`Asynchronous Processing:`**: Commands are handled asynchronously via Kafka to improve throughput.
- **`Saga Pattern:`**: Keeps track of distributed transaction state for each order.
- **`Event-Driven:`**: Uses Kafka topics for reliable communication with other microservices.
- **`Redis Integration:`**: Provides temporary storage for order saga data, enabling retries and state recovery.
- **`Separation of Concerns:`**: Decouples order creation, validation, and external service integration.


## Benefits

- **`Reliable:`**: Ensures no order is lost even if external systems fail.
- **`Scalable:`**: Handles multiple orders in parallel using asynchronous Kafka processing.
- **`Maintainable:`**: Clean separation between domain logic, messaging, and external integration.
- **`Resilient:`**: Supports retries and temporary storage of order state using Redis.
