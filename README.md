# Proyecto: Sistema de Gestión de Restaurante

## Tabla de Contenidos

- [Descripción](#descripción)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Configuración](#configuración)
    - [Configuración de Order](#configuración-de-order)
    - [Configuración de Kitchen](#configuración-de-kitchen)
    - [Configuración de Store](#configuración-de-store)
- [Uso](#uso)
    - [Uso de Order](#uso-de-order)
    - [Uso de Kitchen](#uso-de-kitchen)
    - [Uso de Store](#uso-de-store)
- [Pruebas](#pruebas)
    - [Pruebas de Order](#pruebas-de-order)
    - [Pruebas de Kitchen](#pruebas-de-kitchen)
    - [Pruebas de Store](#pruebas-de-store)
- [Dependencias](#dependencias)
- [Licencia](#licencia)

# Microservices Architecture for Restaurant System

Este proyecto implementa una arquitectura de microservicios utilizando Spring Boot y Netflix OSS, con múltiples servicios interconectados para gestionar el flujo de pedidos, cocina e inventarios en un sistema de restaurante. A continuación, se detallan los componentes clave y su función en el sistema.- **Order**: Gestión de pedidos.

## Arquitectura

La arquitectura del sistema está diseñada como un ecosistema de microservicios. A continuación, se describen los componentes principales que conforman este sistema:

### Componentes Principales

#### **1. Eureka Service (Discovery Server)**
- **Rol:**  
  Permite la descubribilidad de los microservicios en el ecosistema. Los servicios se registran aquí para ser localizados por otros servicios.
- **Tecnologías:**  
  Netflix Eureka.

#### **2. Config Server**
- **Rol:**  
  Gestiona y centraliza la configuración de todos los microservicios. Facilita la configuración dinámica de las aplicaciones sin necesidad de reiniciar los servicios.
- **Tecnologías:**  
  Spring Cloud Config.

#### **3. Config Client**
- **Rol:**  
  Clientes de configuración que obtienen sus configuraciones desde el Config Server.
- **Tecnologías:**  
  Spring Cloud Config Client.

#### **4. Actuator**
- **Rol:**  
  Proporciona métricas, información de salud, y otros endpoints para monitorizar y gestionar el estado de los microservicios.
- **Tecnologías:**  
  Spring Boot Actuator.

#### **5. Gateway**
- **Rol:**  
  Punto de entrada único para las solicitudes HTTP. Se encarga de la autenticación, autorización y enrutamiento a los microservicios internos.
- **Tecnologías:**  
  Spring Cloud Gateway, Spring WebFlux, Netflix OSS.

#### **6. Order Service**
- **Rol:**  
  Gestiona el proceso de creación y seguimiento de pedidos.
- **Tecnologías:**  
  Spring Boot.

#### **7. Kitchen Service**
- **Rol:**  
  Se encarga de procesar los pedidos de cocina y actualizar el estado de los mismos.
- **Tecnologías:**  
  Spring Boot.

#### **8. Store Service**
- **Rol:**  
  Gestiona los ingredientes e inventarios, y coordina con otros servicios para mantener el inventario actualizado.
- **Tecnologías:**  
  Spring Boot.

---

## Requisitos Previos

- Docker
- Java 17
- Node.js 18 (si aplica)
- MongoDB Atlas

## Configuración

Instrucciones específicas para configurar los servicios.

## Uso

Cómo interactuar con los diferentes servicios.

## Pruebas

Cómo ejecutar pruebas unitarias e integraciones.

## Dependencias

Lista de dependencias clave utilizadas en el proyecto.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.
## Estructura del Proyecto

```plaintext
Proyecto/
├── order/
│   ├── src/
│   ├── README.md
│   └── ...
├── kitchen/
│   ├── src/
│   ├── README.md
│   └── ...
├── store/
│   ├── src/
│   ├── README.md
│   └── ...
└── README.md

