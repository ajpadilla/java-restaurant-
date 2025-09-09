package restaurant.order.menu.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.menu.domain.PlateMother;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JpaPlateRepositoryShould {

    @Autowired
    private PlateRepository jpaPlateRepository;

    @Test
    void saveAndRetrieveAPlateWithIngredients() {
        // Arrange
        Plate plate = PlateMother.random();

        // Act
        jpaPlateRepository.save(plate);
        Optional<Plate> foundPlateOpt = jpaPlateRepository.search(plate.getId());

        // Assert
        assertTrue(foundPlateOpt.isPresent(), "Plate should be found after saving");
        Plate foundPlate = foundPlateOpt.get();

        // Plate basic equality (id + name)
        assertEquals(plate.getId(), foundPlate.getId());
        assertEquals(plate.getName(), foundPlate.getName());

        // Ingredients deep equality
        assertEquals(plate.getIngredients().size(), foundPlate.getIngredients().size(),
                "Ingredients list size should match");
        assertIterableEquals(plate.getIngredients(), foundPlate.getIngredients(),
                "Ingredients should match exactly");
    }
}
