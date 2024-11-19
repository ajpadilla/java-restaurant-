package restaurant.order.plate.infrastructure.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.ingredients.domain.IngredientRepository;
import restaurant.order.plate.domain.PlateIdMother;
import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plate.domain.PlateNameMother;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PlateControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected IngredientRepository jpaIngredientRepository;

    @Autowired
    protected PlateRepository plateRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRegisterAPlate() throws Exception {

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            ingredients.add(ingredient);
            this.jpaIngredientRepository.save(ingredient);
        }

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("    \"id\": \"550e8400-e29b-41d4-a716-446655440000\",\n");
        jsonBuilder.append("    \"name\": \"Familiar Pizza\",\n");
        jsonBuilder.append("    \"ingredientsIds\": [\n");

        // Agregar los IDs de ingredientes
        String ingredientIds = ingredients.stream()
                .map(ingredient -> "\"" + ingredient.getId().getValue() + "\"")
                .collect(Collectors.joining(",\n"));

        jsonBuilder.append(ingredientIds);
        jsonBuilder.append("\n    ]\n");
        jsonBuilder.append("}");

        String jsonPayload = jsonBuilder.toString();
        this.mockMvc.perform(post("/api/v1/plates/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());
    }

    @Test
    public void testFindAnExistingPlate() throws Exception {
        Plate plate = PlateMother.random();
        this.plateRepository.save(plate);

        this.mockMvc.perform(get("/api/v1/plates/" + plate.getId().getValue()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(plate.getId().getValue())))
                .andExpect(jsonPath("$.name", is(plate.getName().getValue())))
                .andExpect(jsonPath("$.ingredients", hasSize(plate.getIngredients().size())));

    }

    @Test
    public void testFindAllExistingPlates() throws Exception {

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            ingredients.add(ingredient);
            this.jpaIngredientRepository.save(ingredient);
        }

        for (int i = 0; i < 20; i++) {
            this.plateRepository.save(PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients));
        }

        MvcResult result = this.mockMvc.perform(get("/api/v1/plates/index")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Captura el contenido de la respuesta como una cadena
        String content = result.getResponse().getContentAsString();

        // Imprime el contenido de la respuesta en la consola
        System.out.println("Response from /api/v1/plates/index: " + content);

        // Aserciones con JUnit 5
        assertNotNull(content, "The response content should not be null");
        assertFalse(content.isEmpty(), "The response content should not be empty");

        // Opcional: Verifica que la respuesta contiene ciertas propiedades
        assertTrue(content.contains("\"content\""), "The response should contain 'content'");
        assertTrue(content.contains("\"pageable\""), "The response should contain 'pageable'");
    }
}
