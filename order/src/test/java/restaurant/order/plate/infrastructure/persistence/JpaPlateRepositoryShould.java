package restaurant.order.plate.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaPlateRepositoryShould {

    @Autowired
    protected PlateRepository JpaPlateRepository;

    @Test
    void SaveAPlateEntity() {
        Plate plate = PlateMother.random();

        this.JpaPlateRepository.save(plate);

        Optional<Plate> foundPlate = this.JpaPlateRepository.search(plate.getId());
        assertTrue(foundPlate.isPresent());
        assertEquals(plate, foundPlate.get());
    }

    @Test
    void ReturnAnExistingPlate() {
        Plate plate = PlateMother.random();

        this.JpaPlateRepository.save(plate);

        Optional<Plate> foundPlate = this.JpaPlateRepository.search(plate.getId());
        assertTrue(foundPlate.isPresent());
        assertEquals(plate, foundPlate.get());
        assertEquals(plate.getIngredients(), foundPlate.get().getIngredients());
        foundPlate.get().getIngredients().forEach(System.out::println);
    }
}
