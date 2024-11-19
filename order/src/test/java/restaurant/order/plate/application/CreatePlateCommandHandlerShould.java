package restaurant.order.plate.application;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.ingredients.domain.IngredientRepository;
import restaurant.order.ingredients.domain.IngredientService;
import restaurant.order.plate.domain.PlateIdMother;
import restaurant.order.plate.domain.PlateNameMother;
import restaurant.order.plates.application.create.CreatePlateCommand;
import restaurant.order.plates.application.create.CreatePlateCommandHandler;
import restaurant.order.plates.application.create.PlateCreator;
import restaurant.order.plates.domain.PlateRepository;
import restaurant.order.shared.Infrastructure.bus.postgresql.PostgreSqlConsumer;
import restaurant.order.shared.Infrastructure.bus.postgresql.PostgreSqlEventBus;
import restaurant.order.shared.domain.bus.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class CreatePlateCommandHandlerShould {

    private CreatePlateCommandHandler handler;

    @Autowired
    private  IngredientRepository jpaIngredientRepository;

    @Autowired
    PlateRepository repository;

     @Autowired
    private IngredientService ingredientService;

    @Autowired
    PostgreSqlConsumer consumer;

    @Autowired
    @Qualifier("postgreSqlEventBus")
    EventBus eventBus;

    @Test
    void createAValidPlate() throws InterruptedException {

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < 5; i++) {
            Ingredient ingredient = IngredientMother.random();
            ingredients.add(ingredient);
            this.jpaIngredientRepository.save(ingredient);
        }

        Thread.sleep(2000);

        List<String> ingredientsIds = ingredients.stream()
                .map(ingredient -> ingredient.getId().getValue())
                .toList();


        this.handler = new CreatePlateCommandHandler(new PlateCreator(this.repository, ingredientService,eventBus));

        for (int i = 0; i < 3; i++) {
            CreatePlateCommand command = new CreatePlateCommand(
                    PlateIdMother.random().getValue(),
                    PlateNameMother.random().getValue(),
                    ingredientsIds
            );
            Thread.sleep(2000);
            this.handler.handle(command);
        }

        // Verifica que el evento se ha guardado en la base de datos
        PostgreSqlEventBus postgreSqlEventBus = (PostgreSqlEventBus) this.eventBus;
        List<Object[]> events = postgreSqlEventBus.findAllEvents(); // Llama a tu método para obtener los eventos
        assertFalse(events.isEmpty(), "No events were published");

        // Imprime los resultados en la consola
       /* for (Object[] event : events) {
            System.out.println("Event ID: " + event[0]);
            System.out.println("Aggregate ID: " + event[1]);
            System.out.println("Event Name: " + event[2]);
            System.out.println("Event Body: " + event[3]);
            System.out.println("Occurred On: " + event[4]);
        }*/

        this.consumer.consume();
        //this.consumer.stop();

    }
}
