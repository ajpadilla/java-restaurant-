package com.restaurant.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // Verificar si existe el header de autorización con formato Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange); // Continuar sin autenticación
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        // Si no se puede extraer el username del token, continuar sin autenticación
        if (userEmail == null) {
            return chain.filter(exchange);
        }

        // Buscar detalles del usuario de forma reactiva
        return userDetailsService.findByUsername(userEmail)
                .flatMap(userDetails -> {
                    // Validar el token con los detalles del usuario
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        // Propagar el contexto de seguridad en el entorno reactivo
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
                    } else {
                        // Token inválido: responder con 403 Forbidden
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                })
                // Si el usuario no se encuentra, continuar con la cadena sin autenticación
                .switchIfEmpty(chain.filter(exchange));
    }
}
