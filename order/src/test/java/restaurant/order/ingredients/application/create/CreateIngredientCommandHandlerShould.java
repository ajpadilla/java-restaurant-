package restaurant.order.ingredients.application.create;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import restaurant.order.ingredients.domain.IngredientRepository;
import restaurant.order.shared.Infrastructure.bus.postgresql.PostgreSqlConsumer;
import restaurant.order.shared.Infrastructure.bus.postgresql.PostgreSqlEventBus;
import restaurant.order.shared.domain.bus.event.EventBus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class CreateIngredientCommandHandlerShould {

    private  CreateIngredientCommandHandler handler;

    @Autowired
    private  IngredientRepository repository;

    @Autowired
    PostgreSqlConsumer consumer;


    @Autowired
    @Qualifier("postgreSqlEventBus")
    EventBus eventBus;

    @Test
    void createAValidIngredient() throws InterruptedException {
        this.handler = new CreateIngredientCommandHandler(new IngredientCreator(this.repository, eventBus));

        for (int i = 0; i < 3; i++) {
            CreateIngredientCommand command = CreateIngredientCommandMother.random();
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
