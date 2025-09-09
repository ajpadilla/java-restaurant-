package restaurant.order.plate.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restaurant.order.plate.domain.PlateIdMother;
import restaurant.order.plate.domain.PlateMother;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.infrastructure.persistence.InMemoryPlateRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryPlateRepositoryShould {

    protected InMemoryPlateRepository inMemoryPlateRepository;

    @BeforeEach
    protected void setUp() {
        this.inMemoryPlateRepository = new InMemoryPlateRepository();
    }

    @Test
    void saveAPlate() {
        this.inMemoryPlateRepository.save(PlateMother.random());
    }

    @Test
    void ReturnAnExistingCourse() {
        Plate plate = PlateMother.random();
        this.inMemoryPlateRepository.save(plate);
        assertEquals(Optional.of(plate), this.inMemoryPlateRepository.search(plate.getId()));
    }

    @Test
    void NotReturnANonExistingPlate() {
        assertFalse(this.inMemoryPlateRepository.search(PlateIdMother.random()).isPresent());
    }

}
