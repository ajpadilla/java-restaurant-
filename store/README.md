# Store Service

Este servicio es parte de un sistema de microservicios diseñado para gestionar la disponibilidad y reservas de ingredientes, así como la creación y seguimiento de pedidos en un restaurante. El servicio se comunica con otros microservicios a través de Kafka y sigue un enfoque basado en eventos para la interacción entre los componentes del sistema.

## Estructura del Proyecto

La arquitectura del proyecto está basada en microservicios con una clara separación de responsabilidades. A continuación se describe cada uno de los componentes principales:

### 1. `config/`

Contiene las configuraciones relacionadas con Kafka.

- **KafkaConsumerConfig.java**: Configuración general de los consumidores de Kafka.
- **KafkaTopicConfig.java**: Configuración de los tópicos utilizados en Kafka.

### 2. `consumer/`

Aquí se encuentran los consumidores de eventos que reciben y procesan los mensajes de Kafka.

- **events/**: Contiene las clases de eventos que representan las distintas notificaciones en el sistema, como `IngredientAvailabilityRequest`, `OrderCompletedEvent`, etc.
- **KafkaConsumer.java**: Consumidor base que maneja los eventos de Kafka.
- **PurchaseOrderConsumer.java** y **StoreOrderConsumer.java**: Consumidores específicos para manejar eventos relacionados con la compra y el pedido de ingredientes.
- **Student.java**: Clase de ejemplo (probablemente ya no se usa, a considerar eliminar si no es necesaria).

### 3. `ingredients/`

Maneja la lógica relacionada con los ingredientes del restaurante.

- **application/**: Contiene servicios de aplicación, como `AvailabilityIngredientService.java`, que gestiona la disponibilidad de los ingredientes.
- **domain/**: Define los modelos del dominio, como `Ingredient.java`, `IngredientId.java`, `IngredientQuantity.java`, y excepciones relacionadas como `IngredientNotFoundException.java`.
- **infrastructure/**: Implementación de la infraestructura, incluyendo la integración con APIs externas (`IngredientPurchaseService.java`) y la persistencia de datos mediante `JpaIngredientRepository.java`.

### 4. `order/`

Encargado de la gestión de pedidos.

- **infrastructure/**: Contiene la infraestructura del servicio de pedidos, como el `OrderController.java` y los objetos de solicitud relacionados, como `CreateOrderRequest.java` y `IngredientRequest.java`.

### 5. `purchases/`

Maneja la lógica de compras y pedidos de ingredientes.

- **domain/**: Define los modelos relacionados con las compras, como `Purchase.java` y `PurchaseId.java`.
- **infrastructure/**: Implementa la infraestructura de persistencia con la clase `JpaPurchaseRepository.java`.

### 6. `shared/`

Contiene clases comunes y utilitarias que pueden ser utilizadas en todo el proyecto.

- **domain/**: Incluye la clase `AggregateRoot.java`, que se utiliza para representar los agregados del dominio, y otras clases comunes como `Identifier.java`, `IntValueObject.java`, y `StringValueObject.java`.
- **infrastructure/**: Contiene implementaciones del bus de eventos (`bus/event`).
- **Utils.java**: Métodos utilitarios que ayudan en varias partes del sistema.

### 7. `StoreApplication.java`

Es la clase principal que arranca la aplicación Spring Boot y configura todos los componentes necesarios.

## Flujo de Trabajo

1. **Creación de Pedido**: El servicio puede recibir solicitudes de creación de pedidos mediante el `OrderController`. Estos pedidos incluyen detalles como los ingredientes y platos solicitados.
2. **Gestión de Ingredientes**: El servicio interactúa con la API de ingredientes para verificar la disponibilidad y realizar reservas, actualizando el estado de los ingredientes en tiempo real.
3. **Eventos**: El servicio está basado en eventos. Los eventos de Kafka, como `IngredientAvailabilityRequest` o `OrderCompletedEvent`, se utilizan para comunicar el estado del sistema y coordinar el flujo entre diferentes microservicios.
4. **Persistencia**: Los datos del servicio, como los pedidos y las compras, se persisten en bases de datos mediante repositorios JPA.

## Dependencias Externas

- **Kafka**: Para la comunicación entre microservicios mediante eventos.
- **Redis**: Utilizado para el manejo de sesiones o almacenamiento temporal.
- **JPA**: Para la persistencia de datos.

## Configuración

Puedes configurar el servicio mediante el archivo `application.yml` en el directorio `resources`. Allí se definen los parámetros relacionados con la conexión a Kafka, la base de datos y otros aspectos del sistema.

## Cómo Ejecutar el Proyecto

1. Asegúrate de tener Java 11 o superior instalado.
2. Clona este repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd <nombre_del_directorio>
