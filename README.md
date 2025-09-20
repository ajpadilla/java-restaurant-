# Restaurant Management System – Microservices Architecture

## Table of Contents

- [Project Overview](#overview)
- [Architecture](#architecture)
- [Core Components](#corecomponents)
- [Prerequisites](#prerequisites)
- [Setup & Configuration](#setup&configuration)
    - [Order Service](#orderservice)
    - [Configuración de Store](#configuración-de-store)
- [Uso](#uso)
    - [Order Service](#order-service)
    - [Kitchen Service](#kitchen-service)
    - [Store Service](#store-service)
- [Usage](#usage)
    - [Order Service](#order-service)
    - [Kitchen Service](#pruebas-de-kitchen)
    - [Store Service](#pruebas-de-store)
- [Dependencies](#dependencias)
- [License](#licencia)

# Project Overview

This project implements a microservices-based restaurant management system using Spring Boot and Netflix OSS. It manages orders, kitchen operations, and inventory in a scalable and decoupled way, using CQRS, Event-Driven Architecture, and Hexagonal Architecture principles to ensure flexibility and maintainability.

## Architecture

The system follows a microservices ecosystem pattern with independent services that communicate through APIs and event-driven messages. Each service encapsulates its domain logic and storage, promoting modularity, scalability, and resilience.

### Core Components

#### **1. Eureka Service (Discovery Server)**
- **Role:**  
  Enables service discovery for all microservices in the ecosystem.
- **Technology:** Netflix Eureka.

#### **2. Config Server**
- **Role:**  
  Centralized configuration management for all microservices. Supports dynamic updates without service restarts.
- **Technology:** Spring Cloud Config.

#### **3. Config Client**
- **Role:**  
  Retrieves configuration data from the Config Server.
- **Technology:**  
  Spring Cloud Config Client.

#### **4. Actuator**
- **Role:**  
  Provides metrics, health checks, and monitoring endpoints for microservices.
- **Technology:**  Spring Boot Actuator.

#### **5. Gateway**
- **Role:**  
  Single entry point for HTTP requests. Handles authentication, authorization, and routing to internal services.
- **Technology:**  
  Spring Cloud Gateway, Spring WebFlux, Netflix OSS.

#### **6. Order Service**
- **Role:**  
  Manages order creation, updates, and tracking.
- **Technology:**  
  Spring Boot, CQRS, Event-Driven, JPA/Hibernate.

#### **7. Kitchen Service**
- **Role:**  
  Processes kitchen orders and updates their status.
- **Technology:**  
  Spring Boot.

#### **8. Store Service**
- **Role:**  
  Manages inventory and ingredients. Coordinates with other services to maintain up-to-date stock.
- **Technology:**  
  Spring Boot.

---

## Prerequisites

- Docker
- Java 17
- Node.js 18 (if applicable)
- MongoDB Atlas

## Setup & Configuration

Instructions to configure each microservice individually.

## Order Service

- **Instructions to configure each microservice individually**
- **Connect to the database and configure environment variables.**

## Store Service

- **Configure inventory database.**
- **Ensure proper coordination with Kitchen and Order services.**

# Usage

## Order Service Usage

- **Create new orders, list orders, or fetch order details via REST API or events.**

## Kitchen Service Usage

- **Receive orders, update preparation status, and notify Order service when complete..**

## Store Service Usage

- **Manage inventory, track ingredients, and update stock levels.**


## Testing

- **Each service includes unit and integration tests.**
- **Run tests with Maven: mvn test**
- **Or use Docker Compose to test the entire ecosystem.**

## Dependencies

- **Spring Boot.**
- **Spring Cloud Netflix OSS (Eureka, Gateway)**
- **Spring Cloud Config**
- **JPA / Hibernate**
- **Kafka / PostgreSQL for event-driven messaging**
- **Docker**

## License

This project is licensed under the MIT License – see the LICENSE file for details.