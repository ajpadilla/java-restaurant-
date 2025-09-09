package restaurant.order.order.infrastructure.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import restaurant.order.menu.domain.*;
import restaurant.order.order.domain.*;
import restaurant.order.menu.domain.PlateIdMother;
import restaurant.order.menu.domain.PlateMother;
import restaurant.order.menu.domain.PlateNameMother;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
public class OrdersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected OrderRepository orderRepository;

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
        }

        this.plate = PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), this.ingredients);
        this.plateRepository.save(this.plate);

       this.foundPlate = this.plateRepository.search(this.plate.getId());
    }


    private List<Ingredient> makeIngredientListToPlate(List<String> ingredientsForPlate) {
        List<Ingredient> ingredientsListToPlate = new ArrayList<Ingredient>();

        ingredientsForPlate.forEach(ingredient ->  {
            ingredientsListToPlate.add(IngredientMother.create(
                    IngredientIdMother.random(),
                    new IngredientName(ingredient),
                    IngredientQuantityMother.random()
            ));
        });

        return ingredientsListToPlate;
    }


    // Prepara el payload JSON
        /*String jsonPayload = String.format(
                "{\"id\":\"550e8400-e29b-41d4-a716-446655440000\", \"plateIds\":%s}",
                new ObjectMapper().writeValueAsString(plateIds)
        );*/
    /*
    @Test
    public void testCreateAOrder() throws Exception{

        List<Plate> plates = new ArrayList<>();
        List<String> plateIds = new ArrayList<>();

        List<Ingredient> ingredientsPlate1 = makeIngredientListToPlate(Arrays.asList(
                "Tomato", "Lemon", "Potato", "Rice", "Ketchup"
        ));
        ingredientsPlate1.forEach(ingredient -> {
            this.jpaIngredientRepository.save(ingredient);
        });

        Plate plate1 = PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredientsPlate1);
        plates.add(plate1);
        plateIds.add(plate1.getId().getValue());
        this.plateRepository.save(plate1);

        List<Ingredient> ingredientsPlate2 = makeIngredientListToPlate(Arrays.asList(
                "Lettuce", "Onion", "Cheese", "Meat", "Chicken"
        ));

        ingredientsPlate2.forEach(ingredient -> {
            this.jpaIngredientRepository.save(ingredient);
        });

        Plate plate2 = PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredientsPlate2);
        plates.add(plate2);
        plateIds.add(plate2.getId().getValue());
        this.plateRepository.save(plate2);

        List<Ingredient> ingredientsPlate3 = makeIngredientListToPlate(Arrays.asList(
                "Lettuce", "Onion", "Cheese", "Meat", "Chicken"
        ));

        ingredientsPlate3.forEach(ingredient -> {
            this.jpaIngredientRepository.save(ingredient);
        });

        Plate plate3 = PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredientsPlate3);
        plates.add(plate3);
        plateIds.add(plate3.getId().getValue());
        this.plateRepository.save(plate3);



        String jsonPayload = String.format(
                "{\"id\":\"%s\", \"plateIds\":%s}",
                OrderIdMother.random().getValue(), // Genera el valor aleatorio
                new ObjectMapper().writeValueAsString(plateIds) // Convierte plateIds a JSON
        );

        // Realiza la petición POST y verifica el estado de la respuesta
        mockMvc.perform(post("/api/v1/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());

    } */

    @Test
    public void testFindAllExistingOrders() throws Exception {

        // Crear y guardar una orden usando la entidad de plato inicializada
        /*Order order = OrderMother.random();
        this.orderRepository.save(order);

        Page<Order> orders = this.orderRepository.searchAll(0,10);

        orders.getContent().stream()
                .map(orderObject -> "Order ID: " + orderObject.getId() + ", Plate: " + orderObject.getPlates())
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
        assertTrue(content.contains("\"pageable\""), "The response should contain 'pageable'");*/
    }
}
