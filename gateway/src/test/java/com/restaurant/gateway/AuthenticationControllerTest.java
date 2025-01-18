package com.restaurant.gateway;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.gateway.auth.AuthenticationRequest;
import com.restaurant.gateway.auth.RegisterRequest;
import com.restaurant.gateway.config.SecurityConfiguration;
import com.restaurant.gateway.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false"
})
@Transactional
@Import(SecurityConfiguration.class)  // Asegúrate de incluir la configuración de seguridad
public class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Crear el payload JSON para registrar un usuario
        String jsonPayload = new ObjectMapper().writeValueAsString(
                RegisterRequest.builder()
                        .firstname("John")
                        .lastname("Doe")
                        .email("john.doe@example.com")
                        .password("password123")
                        .role(Role.valueOf("MANAGER"))
                        .build()
        );

        // Realizar la petición POST y verificar el estado de la respuesta
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andReturn();

        // Captura el contenido de la respuesta como una cadena
        String content = result.getResponse().getContentAsString();

        // Imprime el contenido de la respuesta en la consola
        System.out.println("Response from /api/v1/auth/register: " + content);

        // Verifica que la respuesta no sea nula y contiene el token
        assertNotNull(content, "The response content should not be null");
        assertTrue(content.contains("token"), "The response should contain a 'token'");
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        // Registrar un usuario antes de autenticarlo
        String registerPayload = new ObjectMapper().writeValueAsString(
                RegisterRequest.builder()
                        .firstname("Jane")
                        .lastname("Doe")
                        .email("jane.doe@example.com")
                        .password("securepass")
                        .role(Role.valueOf("ADMIN"))
                        .build()
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isOk());

        // Crear el payload JSON para autenticar al usuario
        String authPayload = new ObjectMapper().writeValueAsString(
                AuthenticationRequest.builder()
                        .email("jane.doe@example.com")
                        .password("securepass")
                        .build()
        );

        // Realizar la petición POST para autenticación
        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authPayload))
                .andExpect(status().isOk())
                .andReturn();

        // Captura el contenido de la respuesta como una cadena
        String content = result.getResponse().getContentAsString();

        // Imprime el contenido de la respuesta en la consola
        System.out.println("Response from /api/v1/auth/authenticate: " + content);

        // Verifica que la respuesta no sea nula y contiene el token
        assertNotNull(content, "The response content should not be null");
        assertTrue(content.contains("token"), "The response should contain a 'token'");
    }

    @Test
    void shouldReturnUnauthorizedForInvalidUser() throws Exception {

        // Crear el payload JSON para autenticar al usuario
        String authPayload = new ObjectMapper().writeValueAsString(
                AuthenticationRequest.builder()
                        .email("jane.doe@example.com")
                        .password("securepass")
                        .build()
        );

        // Realizar la solicitud POST al endpoint /api/v1/auth/authenticate
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authPayload))
                .andExpect(status().isUnauthorized()) // Verificar que el status sea 401
                .andExpect(jsonPath("$.error").exists()) // Verificar que la respuesta contiene un campo 'error'
                .andExpect(jsonPath("$.message").value("Invalid credentials")); // Mensaje de error esperado
    }

    @Test
    void shouldReturnForbiddenForGetManagementWithoutPermissions() throws Exception {
        // Simular que el usuario no tiene el rol adecuado
        mockMvc.perform(get("/api/v1/management")
                        .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isForbidden()); // Esperar respuesta 403 Forbidden
    }

}