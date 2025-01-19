# Restaurant Order Management

Este es un sistema de gestión de pedidos para un restaurante, desarrollado con Java (Spring Boot) y Kafka para la comunicación entre microservicios. El sistema se compone de varios módulos que gestionan diferentes aspectos del proceso de pedidos, como ingredientes, platos, y la integración con Kafka para la mensajería asíncrona.

## Estructura del Proyecto

# Restaurant Order System

Este proyecto es una implementación de un sistema de gestión de órdenes para un restaurante. Utiliza una arquitectura basada en microservicios con Spring Boot, y se integra con Kafka para el procesamiento de mensajes y eventos.

## Estructura del Proyecto

La estructura de carpetas y archivos sigue principios de organización clara y separación de responsabilidades. Aquí se describe cada parte clave del sistema.

```bash
restaurant-order-system
├── main
│   ├── java
│   │   └── restaurant
│   │       └── order
│   │           ├── config                   # Configuración de servicios y Kafka
│   │           │   ├── KafkaConfig.java     # Configuración principal de Kafka
│   │           │   ├── KafkaTopicConfig.java# Configuración de los tópicos de Kafka
│   │           │   └── PayPalConfig.java    # Configuración para PayPal
│   │           ├── consumer                # Lógica para consumo de mensajes de Kafka
│   │           │   └── KitchenService.java  # Servicio que maneja los mensajes relacionados con la cocina
│   │           ├── ingredients             # Módulo de ingredientes
│   │           │   ├── application         # Aplicación de ingredientes
│   │           │   │   ├── create          # Lógica para la creación de ingredientes
│   │           │   │   ├── find            # Lógica para la búsqueda de ingredientes
│   │           │   │   └── increment       # Lógica para incrementar la cantidad de ingredientes
│   │           │   ├── domain              # Entidades y lógica de negocio de ingredientes
│   │           │   │   ├── IngredientCreatedDomainEvent.java # Evento de creación de ingrediente
│   │           │   │   ├── IngredientId.java
│   │           │   │   ├── Ingredient.java
│   │           │   │   ├── IngredientName.java
│   │           │   │   ├── IngredientNotFoundException.java  # Excepción cuando no se encuentra un ingrediente
│   │           │   │   ├── IngredientQuantity.java
│   │           │   │   ├── IngredientRepository.java
│   │           │   │   └── IngredientService.java
│   │           │   └── infrastructure       # Implementaciones de infraestructura para ingredientes
│   │           │       ├── controller      # Controladores de la API para ingredientes
│   │           │       ├── entity          # Entidades JPA de ingredientes
│   │           │       └── persistence     # Repositorios y lógica de persistencia
│   │           ├── kafka                    # Configuración y lógica relacionada con Kafka
│   │           │   ├── KafkaJsonProducer.java  # Productores de mensajes JSON
│   │           │   ├── KafkaOrderJsonService.java # Servicio para manejar órdenes Kafka
│   │           │   ├── KafkaProducer.java       # Productores de Kafka
│   │           │   ├── KafkaProducerService.java  # Servicio para la producción de eventos en Kafka
│   │           │   └── payload                 # Estructuras de datos para enviar a Kafka
│   │           │       └── Student.java
│   │           ├── order                    # Módulo de órdenes
│   │           │   ├── application          # Aplicación de órdenes
│   │           │   │   ├── create           # Lógica para la creación de órdenes
│   │           │   │   └── find             # Lógica para la búsqueda de órdenes
│   │           │   ├── domain               # Entidades y lógica de negocio de órdenes
│   │           │   │   ├── CastOrderToJsonService.java # Servicio para convertir ordenes a JSON
│   │           │   │   ├── OrderCreatedDomainEvent.java # Evento de creación de orden
│   │           │   │   ├── OrderId.java
│   │           │   │   ├── Order.java
│   │           │   │   ├── OrderRepository.java
│   │           │   │   └── PaymentOrderProcessor.java  # Procesamiento de pagos para las órdenes
│   │           │   └── infrastructure        # Implementaciones de infraestructura para órdenes
│   │           │       ├── controller       # Controladores de la API para órdenes
│   │           │       ├── entity           # Entidades JPA de órdenes
│   │           │       ├── paymentmethods   # Métodos de pago relacionados con órdenes
│   │           │       └── persistence      # Repositorios y lógica de persistencia de órdenes
│   │           ├── OrderApplication.java   # Clase principal para iniciar la aplicación
│   │           ├── plates                   # Módulo de platos
│   │           │   ├── application         # Lógica para la creación y búsqueda de platos
│   │           │   ├── domain              # Entidades y lógica de negocio de platos
│   │           │   └── infrastructure      # Implementación de infraestructura para platos
│   │           ├── rest                     # Endpoints de REST para manejo de mensajes
│   │           │   └── MessageController.java # Controlador para manejar mensajes
│   │           └── shared                   # Componentes compartidos entre módulos
│   │               ├── domain              # Clases base para entidades y objetos de valor
│   │               ├── Infrastructure       # Infraestructura compartida (controladores, buses, etc.)
│   │               └── test                 # Clases base para pruebas
│   └── resources
│       ├── application.yml  # Configuración principal de la aplicación
│       ├── static           # Archivos estáticos (si aplican)
│       └── templates        # Plantillas (si aplican)
└── test
    └── java
        └── restaurant
            └── order
                ├── ingredients               # Pruebas para el módulo de ingredientes
                ├── KitchenApplicationTests.java # Pruebas generales para la aplicación de cocina
                ├── order                     # Pruebas para el módulo de órdenes
                ├── plate                     # Pruebas para el módulo de platos
                └── rest                      # Pruebas para el controlador de mensajes

```

# Descripción de las Carpetas

- **main/java/restaurant/order**: Contiene el código fuente de la aplicación, organizado en módulos como ingredientes, órdenes, platos, y Kafka.

- **config**: Archivos de configuración, como la configuración de Kafka y PayPal.

- **consumer**: Contiene servicios encargados de consumir los mensajes provenientes de Kafka.

- **ingredients**: Módulo que gestiona la creación, consulta e incremento de ingredientes.

- **kafka**: Contiene los productores y servicios de Kafka para enviar eventos relacionados con las órdenes y otros procesos.

- **order**: Módulo que gestiona la creación, consulta y procesamiento de órdenes.

- **rest**: Controlador para manejar los mensajes que la aplicación recibe.

- **shared**: Contiene clases y servicios comunes como objetos de valor y controladores base.

- **test/java/restaurant/order**: Contiene las pruebas unitarias y de integración para cada módulo.

    - **ingredients**: Pruebas específicas para el módulo de ingredientes.

    - **order**: Pruebas para el módulo de órdenes.

    - **rest**: Pruebas para los controladores REST.

# Tecnologías Utilizadas

- **Spring Boot**: Para la construcción de microservicios.

- **Kafka**: Para el manejo de mensajes entre servicios.

- **JUnit / Mockito**: Para pruebas unitarias.

- **PostgreSQL**: Para persistencia de datos.

# Flujo de Trabajo

1. **Creación de Pedido**: El servicio puede recibir solicitudes de creación de pedidos mediante el `OrderController`. Estos pedidos incluyen detalles como los ingredientes y platos solicitados.

2. **Gestión de Ingredientes**: El servicio interactúa con la API de ingredientes para verificar la disponibilidad y realizar reservas, actualizando el estado de los ingredientes en tiempo real.

3. **Eventos**: El servicio está basado en eventos. Los eventos de Kafka, como `IngredientAvailabilityRequest` o `OrderCompletedEvent`, se utilizan para comunicar el estado del sistema y coordinar el flujo entre diferentes microservicios.

4. **Persistencia**: Los datos del servicio, como los pedidos y las compras, se persisten en bases de datos mediante repositorios JPA.

# Dependencias Externas

- **Kafka**: Para la comunicación entre microservicios mediante eventos.

- **Redis**: Utilizado para el manejo de sesiones o almacenamiento temporal.

- **JPA**: Para la persistencia de datos.

# Configuración

Puedes configurar el servicio mediante el archivo `application.yml` en el directorio `resources`. Allí se definen los parámetros relacionados con la conexión a Kafka, la base de datos y otros aspectos del sistema.

# Cómo Ejecutar el Proyecto

1. Asegúrate de tener Java 11 o superior instalado.
2. Clona este repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd <nombre_del_directorio>
