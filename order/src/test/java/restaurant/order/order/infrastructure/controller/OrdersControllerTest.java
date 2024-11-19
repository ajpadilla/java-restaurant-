package restaurant.order.order.infrastructure.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.ingredients.domain.IngredientRepository;
import restaurant.order.order.domain.*;
import restaurant.order.plate.domain.PlateIdMother;
import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plate.domain.PlateNameMother;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class OrdersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected IngredientRepository jpaIngredientRepository;

    @Autowired
    protected PlateRepository plateRepository;

    protected List<Ingredient> ingredients;
    protected Plate plate;

    protected Optional<Plate> foundPlate;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Inicialización de entidades comunes
        this.ingredients = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            this.ingredients.add(ingredient);
            this.jpaIngredientRepository.save(ingredient);
        }

        this.plate = PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), this.ingredients);
        this.plateRepository.save(this.plate);

       this.foundPlate = this.plateRepository.search(this.plate.getId());
    }

    @Test
    public void testCreateAOrder() throws Exception{

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            ingredients.add(ingredient);
            this.jpaIngredientRepository.save(ingredient);
        }

        Plate plate = PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients);

        this.plateRepository.save(plate);

        // Prepara el payload JSON
        String jsonPayload = String.format(
                "{\"id\":\"550e8400-e29b-41d4-a716-446655440000\", \"plateId\":\"%s\"}",
                plate.getId().getValue()
        );
        // Realiza la petición POST y verifica el estado de la respuesta
        mockMvc.perform(post("/api/v1/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());

    }

    @Test
    public void testFindAllExistingOrders() throws Exception {

        // Crear y guardar una orden usando la entidad de plato inicializada
        Order order = OrderMother.create(OrderIdMother.random(), this.foundPlate.get());
        this.orderRepository.save(order);

        Page<Order> orders = this.orderRepository.searchAll(0,10);

        orders.getContent().stream()
                .map(orderObject -> "Order ID: " + orderObject.getId() + ", Plate: " + orderObject.getPlate())
                .forEach(System.out::println);

        MvcResult result = this.mockMvc.perform(get("/api/v1/orders/index")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Captura el contenido de la respuesta como una cadena
        String content = result.getResponse().getContentAsString();

        // Imprime el contenido de la respuesta en la consola
        System.out.println("Response from /api/v1/orders/index: " + content);

        // Aserciones con JUnit 5
        assertNotNull(content, "The response content should not be null");
        assertFalse(content.isEmpty(), "The response content should not be empty");

        // Opcional: Verifica que la respuesta contiene ciertas propiedades
        assertTrue(content.contains("\"content\""), "The response should contain 'content'");
        assertTrue(content.contains("\"pageable\""), "The response should contain 'pageable'");
    }
}
