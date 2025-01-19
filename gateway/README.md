# Sistema de Microservicios

Este sistema está basado en una arquitectura de microservicios utilizando el stack de Netflix, que incluye servicios como Eureka para el descubrimiento de servicios, Spring Cloud Config para la configuración centralizada, Spring Cloud Gateway para la gestión del tráfico y seguridad, y Spring Boot para el desarrollo de los servicios.

## Componentes del Sistema

### 1. **Eureka Discovery Service**
Eureka es un servicio de descubrimiento que permite a los microservicios registrarse y descubrirse entre sí. Esto facilita la comunicación entre servicios sin necesidad de conocer direcciones IP estáticas o configuraciones específicas.

### 2. **Config Server**
El servidor de configuración gestiona y centraliza la configuración de todos los microservicios, permitiendo la carga dinámica de configuraciones desde un repositorio como Git, base de datos o archivos.

### 3. **Config Client**
Los clientes de configuración se conectan al Config Server para obtener las configuraciones correspondientes a sus servicios. Esto permite mantener la configuración centralizada y flexible.

### 4. **Spring Cloud Gateway**
Spring Cloud Gateway actúa como un punto de entrada único para todas las solicitudes que llegan al sistema. Permite enrutar solicitudes, aplicar políticas de seguridad y administrar el tráfico de forma eficiente.

### 5. **Actuator**
Spring Actuator proporciona una serie de puntos finales para monitorear el estado de la aplicación, como información de salud, métricas, auditoría y más.

## Configuración de Seguridad en Gateway

La seguridad en el gateway es gestionada utilizando **Spring Security** y **WebFlux Security**, configurando una política de autenticación y autorización adecuada para los servicios expuestos.

### Código de Configuración de Seguridad

El siguiente código configura los filtros de seguridad en el `Gateway` utilizando Spring WebFlux:

```java
package com.restaurant.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static com.restaurant.gateway.user.Permission.*;
import static com.restaurant.gateway.user.Role.ADMIN;
import static com.restaurant.gateway.user.Role.MANAGER;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity  // Asegura que Spring Security Reactive está habilitado
public class WebFluxSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/orders/healthcheck").permitAll()  // Permite acceso sin autenticación
                        .anyExchange().authenticated()  // Requiere autenticación para cualquier otra solicitud
                )
                .formLogin(withDefaults());  // Configuración de autenticación con formulario

        return http.build();
    }
}
