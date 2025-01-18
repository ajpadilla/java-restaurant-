package restaurant.order.order.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.domain.IngredientMother;
import restaurant.order.order.domain.Order;
import restaurant.order.order.domain.OrderIdMother;
import restaurant.order.order.domain.OrderMother;
import restaurant.order.order.domain.OrderRepository;
import restaurant.order.plate.domain.PlateIdMother;
import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plate.domain.PlateNameMother;
import restaurant.order.plates.domain.Plate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaOrderRepositoryShould {

    @Autowired
    protected OrderRepository jpaOrderRepository;

    @Test
    void SaveAPlateEntity() {
        Order order = OrderMother.random();

        this.jpaOrderRepository.save(order);

        Optional<Order> foundOrder = this.jpaOrderRepository.search(order.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals(order, foundOrder.get());
    }

    @Test
    void ReturnAnExistingPlate() {
        // Crear una orden con múltiples platos
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(IngredientMother.random());
        }

        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 3; i++) { // Supongamos que queremos 3 platos
            plates.add(PlateMother.create(PlateIdMother.random(), PlateNameMother.random(), ingredients));
        }

        Order order = OrderMother.create(OrderIdMother.random(), plates);

        // Guardar la orden
        this.jpaOrderRepository.save(order);

        // Buscar la orden
        Optional<Order> foundOrder = this.jpaOrderRepository.search(order.getId());
        assertTrue(foundOrder.isPresent());

        // Verificar que las listas de platos sean iguales
        List<Plate> foundPlates = foundOrder.get().getPlates();
        assertEquals(order.getPlates().size(), foundPlates.size());

        // Comparar cada plato individualmente
        for (int i = 0; i < order.getPlates().size(); i++) {
            assertEquals(order.getPlates().get(i), foundPlates.get(i));
        }

        // Imprimir los ingredientes de cada plato
        foundPlates.forEach(plate -> plate.getIngredients().forEach(System.out::println));
    }
}
