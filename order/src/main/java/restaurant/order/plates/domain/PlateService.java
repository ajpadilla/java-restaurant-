package restaurant.order.plates.domain;

import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.InvalidUUIDException;
import restaurant.order.shared.domain.Utils;

@Service
public class PlateService {

    private final PlateRepository repository;


    public PlateService(PlateRepository repository) {
        this.repository = repository;
    }

    public Plate findAnExistingPlate(PlateId id) {

        if (!Utils.isUUID(id.getValue())) {
            throw new InvalidUUIDException("Invalid UUID string: " + id.getValue());
        }

        return this.repository.search(id).orElseThrow(()-> new PlateNotFoundException("Ingredient not found: " + id.getValue()));
    }

}
