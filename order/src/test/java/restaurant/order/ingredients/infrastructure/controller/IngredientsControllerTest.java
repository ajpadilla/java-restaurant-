package restaurant.order.ingredients.infrastructure.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.ingredients.domain.IngredientRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class IngredientsControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    protected IngredientRepository jpaIngredientRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRegisterIngredient() throws Exception {
        // Prepara el payload JSON
        String jsonPayload = "{\"id\":\"550e8400-e29b-41d4-a716-446655440000\", \"name\":\"Tomato\", \"quantity\":10}";

        // Realiza la petición POST y verifica el estado de la respuesta
        mockMvc.perform(post("/api/v1/ingredients/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());
    }


    @Test
    public void testFindAnExistingIngredient() throws Exception {

        Ingredient ingredient = IngredientMother.random();

        this.jpaIngredientRepository.save(ingredient);

        this.mockMvc.perform(get("/api/v1/ingredients/" + ingredient.getId().getValue()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(ingredient.getId().getValue())))
                .andExpect(jsonPath("$.name", is(ingredient.getName().getValue())))
                .andExpect(jsonPath("$.quantity", is(ingredient.getQuantity().getValue())));

    }

    @Test
    public void testFindAllExistingIngredients() throws Exception  {

        for (int i = 0; i < 20; i++) {
            this.jpaIngredientRepository.save(IngredientMother.random());
        }

        MvcResult result = this.mockMvc.perform(get("/api/v1/ingredients/index")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Captura el contenido de la respuesta como una cadena
        String content = result.getResponse().getContentAsString();

        // Imprime el contenido de la respuesta en la consola
        System.out.println("Response from /api/v1/ingredients/index: " + content);

        // Aserciones con JUnit 5
        assertNotNull(content, "The response content should not be null");
        assertFalse(content.isEmpty(), "The response content should not be empty");

        // Opcional: Verifica que la respuesta contiene ciertas propiedades
        assertTrue(content.contains("\"content\""), "The response should contain 'content'");
        assertTrue(content.contains("\"pageable\""), "The response should contain 'pageable'");
    }
}
