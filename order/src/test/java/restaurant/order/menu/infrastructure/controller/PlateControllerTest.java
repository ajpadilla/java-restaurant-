package restaurant.order.menu.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import restaurant.order.menu.domain.Ingredient;
import restaurant.order.menu.domain.IngredientMother;
import restaurant.order.menu.domain.IngredientRepository;
import restaurant.order.menu.domain.PlateIdMother;
import restaurant.order.menu.domain.PlateMother;
import restaurant.order.menu.domain.PlateNameMother;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateRepository;
import restaurant.order.menu.infrastructure.controller.request.CreatePlateRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
    protected PlateRepository plateRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRegisterAPlate() throws Exception {
        // Arrange: create and persist ingredients (domain objects)
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            ingredients.add(ingredient);
        }

        // Build ingredient requests for the plate
        /*List<CreatePlateRequest.PlateIngredientRequest> ingredientRequests = ingredients.stream()
                .map(i -> CreatePlateRequest.PlateIngredientRequest.builder()
                        .ingredientId(i.getId().getValue())
                        .ingredientName(i.getName().getValue())
                        .requiredQuantity(i.getQuantity().getValue())
                        .build())
                .toList();*/


        List<CreatePlateRequest.PlateIngredientRequest> ingredientRequests = List.of(
                CreatePlateRequest.PlateIngredientRequest.builder()
                        .ingredientId("11111111-1111-1111-1111-111111111111")
                        .ingredientName("Tomato")
                        .requiredQuantity(5)
                        .build(),
                CreatePlateRequest.PlateIngredientRequest.builder()
                        .ingredientId("22222222-2222-2222-2222-222222222222")
                        .ingredientName("Cheese")
                        .requiredQuantity(3)
                        .build()
        );

        // Build the plate request DTO
        CreatePlateRequest request = CreatePlateRequest.builder()
                .id(UUID.randomUUID().toString())
                .name("Familiar Pizza")
                .ingredients(ingredientRequests)
                .build();

        // Convert to JSON
        String jsonPayload = objectMapper.writeValueAsString(request);

        // Act + Assert
        mockMvc.perform(post("/api/v1/plates/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print()) // 👈 shows full request/response details in console
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
