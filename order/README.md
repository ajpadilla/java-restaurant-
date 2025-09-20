# 🍽️ Restaurant Order Management System

A modern, scalable **Restaurant Order System** built with **Java (Spring Boot)**, **Kafka**, and **PostgreSQL**.  
The system follows **Domain-Driven Design (DDD)**, **Hexagonal Architecture**, and **CQRS + Event Sourcing** patterns, ensuring flexibility, maintainability, and scalability.

---

## 📂 Project Structure

The project is organized into modules following **DDD bounded contexts** and **Hexagonal Architecture**:

```bash
├── config              # Kafka, Debezium, PayPal configuration
├── consumer            # Kafka consumers (e.g., KitchenService)
├── kafka               # Kafka producers and payload services
├── menu                # Plates & Ingredients (domain, application, infra)
│   ├── application     # Use cases (commands, queries, handlers)
│   ├── domain          # Aggregates, entities, value objects, domain events
│   └── infrastructure  # Controllers, persistence, adapters
├── order               # Orders (domain, application, infra)
│   ├── application     # Create/find orders (CQRS handlers)
│   ├── domain          # Aggregate Root (Order), repositories, domain events
│   └── infrastructure  # Controllers, persistence, payment adapters
├── rest                # REST controllers for messaging
├── shared              # Common domain abstractions (EventBus, CommandBus, ValueObjects)
└── test                # Unit & integration tests


Client HTTP POST /orders
        │
        ▼
OrdersController
        │
        ▼
CommandBus.dispatch(CreateOrderCommand)
        │
        ▼
CreateOrderCommandHandler
        │
        ▼
OrderCreator (Domain Layer: Aggregate Root)
        │
        ├─ Validates business rules
        ├─ Saves order via OrderRepository (JPA)
        └─ Publishes Domain Events
                │
                ▼
Transactional Outbox → Event Consumers / Kafka → Other Microservices


Reading orders uses CQRS + caching for fast, scalable queries:
Client HTTP GET /orders?page=0&size=10
        │
        ▼
OrdersController
        │
        ▼
QueryBus.ask(FindOrdersQuery)
        │
        ▼
FindOrdersQueryHandler
        │
        ▼
OrdersFinder
        │
   ┌────┴─────┐
   │ Cache Hit │ → return instantly
   └────┬─────┘
        │
   Cache Miss → repository.searchAll(page, size)
        │
   Save result in cache
        ▼
Return paginated list of orders

Business logic is isolated from infrastructure, making the system adaptable and future-proof:

+------------------------+
|   Application Layer     |
|  (use cases, services)  |
+-----------+-------------+
            │
            ▼
+------------------------+
|     Domain Layer        |
|   -------------------   |
|   Aggregate Root (Order)|
|   + business rules      |
|   + domain events       |
|   Other Entities        |
|   (Plates, Ingredients) |
+-----------+-------------+
            │  (Port: OrderRepository interface)
            ▼
+------------------------+
|  Infrastructure Layer   |
|   JPA Repository        |
|   + maps Aggregate <->  |
|     Database Entity     |
|   OrderEntity (DB model)|
+-----------+-------------+
            │
            ▼
+------------------------+
|   Database / Storage    |
+------------------------+
