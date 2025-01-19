# Kitchen Microservice

Este microservicio es parte de un sistema para gestionar la cocina en un restaurante. Se encarga de procesar pedidos, reservar ingredientes y realizar validaciones. A continuación, se detalla la estructura del proyecto.

## Estructura del Proyecto

```plaintext
kitchen/
├── main
│   ├── java
│   │   └── restautant
│   │       └── kitchen
│   │           ├── configuration
│   │           │   └── RedisConfig.java          # Configuración de Redis
│   │           ├── consumer
│   │           │   ├── events
│   │           │   │   ├── IngredientAvailabilityRequest.java  # Evento para disponibilidad de ingredientes
│   │           │   │   ├── IngredientReservedEvent.java       # Evento de ingrediente reservado
│   │           │   │   └── OrderCompletedEvent.java          # Evento de orden completada
│   │           │   └── KitchenService.java                   # Servicio que consume eventos y procesa pedidos
│   │           ├── ingredient
│   │           │   ├── application
│   │           │   ├── domain
│   │           │   │   ├── Ingredient.java                  # Entidad Ingrediente
│   │           │   │   └── IngredientRepository.java        # Repositorio de ingredientes
│   │           │   └── infrastructure
│   │           │       └── persistence                     # Persistencia de ingredientes
│   │           ├── order
│   │           │   ├── application
│   │           │   │   └── OrderValidationService.java      # Servicio de validación de pedidos
│   │           │   ├── domain
│   │           │   │   ├── Order.java                      # Entidad Pedido
│   │           │   │   └── OrderRepository.java            # Repositorio de pedidos
│   │           │   └── infrastructure
│   │           │       └── persistence                     # Persistencia de pedidos
│   │           ├── plate
│   │           │   ├── domain
│   │           │   │   ├── Plate.java                      # Entidad Plato
│   │           │   │   └── PlateRepository.java            # Repositorio de platos
│   │           └── shared
│   │               ├── domain
│   │               │   └── AggregateRoot.java              # Clase base para agregados
│   │               ├── infrastructure
│   │               │   ├── feignclient                    # Cliente Feign para integración externa
│   │               │   └── spring
│   │               └── test
│   │                   ├── IntegerMother.java              # Clase para generación de datos de prueba
│   │                   └── UuidMother.java                 # Generador de UUIDs para pruebas
│   └── resources
│       ├── application.yml                                  # Configuración de la aplicación
│       ├── static
│       └── templates
└── test
    └── java
        └── restautant
            └── kitchen
                ├── application
                │   └── OrderValidationServiceTest.java    # Test para el servicio de validación de pedidos
                ├── order
                │   └── domain
                │       └── OrderMother.java               # Datos de prueba para la entidad Pedido
                ├── plate
                │   └── domain
                │       └── PlateMother.java               # Datos de prueba para la entidad Plato
                └── KitchenApplicationTests.java           # Test para la aplicación en general

```

# Kitchen Microservice

Este repositorio contiene el código fuente del microservicio `kitchen`, que se encarga de la gestión de ingredientes y platos dentro de un sistema de pedidos en un restaurante. Utiliza tecnologías como Spring Boot, Kafka, y Redis para la integración y procesamiento de eventos.

## Estructura de Carpetas y Archivos

### `main/java/restaurant/kitchen`

Contiene el código fuente principal de la aplicación.

- **`configuration`**: Configuraciones de infraestructura, como Redis.
- **`consumer`**: Consumo de eventos de Kafka y procesamiento de pedidos.
- **`ingredient`**: Lógica relacionada con la gestión de ingredientes.
- **`order`**: Lógica relacionada con los pedidos.
- **`plate`**: Lógica relacionada con los platos del menú.
- **`shared`**: Componentes reutilizables como clases base, servicios compartidos y clientes Feign.

### `main/resources`

Archivos de configuración y recursos estáticos.

- **`application.yml`**: Configuración principal de la aplicación.

### `test`

Contiene los archivos de prueba unitarios y de integración.

- **`application`**: Pruebas para servicios y validaciones.
- **`order`**: Pruebas para la entidad `Order`.
- **`plate`**: Pruebas para la entidad `Plate`.
- **`KitchenApplicationTests.java`**: Pruebas generales para la aplicación.

## Requisitos

- Java 11 o superior.
- Spring Boot.
- Kafka.
- Redis.

## Instalación

1. Clona el repositorio:

    ```bash
    git clone <url-del-repositorio>
    cd kitchen
    ```

2. Configura las dependencias en tu IDE o mediante Maven:

    ```bash
    mvn install
    ```

3. Configura los archivos de propiedades (`application.yml`) con las credenciales necesarias para Kafka y Redis.

4. Ejecuta la aplicación:

    ```bash
    mvn spring-boot:run
    ```

## Contribución

Si deseas contribuir, por favor abre un pull request describiendo tus cambios. Asegúrate de que las pruebas sean aprobadas antes de enviar la solicitud.
