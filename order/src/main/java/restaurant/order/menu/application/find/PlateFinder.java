package restaurant.order.menu.application.create.find;

import org.springframework.stereotype.Service;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateNotFoundException;
import restaurant.order.plates.domain.PlateRepository;

@Service
public class PlateFinder {

    private final PlateRepository repository;


    public PlateFinder(PlateRepository repository) {
        this.repository = repository;
    }

    public PlateResponse find(PlateId id) {
        Plate plate = this.repository.search(id).orElseThrow(
                () -> new PlateNotFoundException("Ingredient not found: " + id.getValue())
        );
        return new PlateResponse(plate);
    }
}
