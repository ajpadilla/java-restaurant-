package restaurant.order.plates.infrastructure.persistence;

import org.springframework.data.domain.Page;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateId;
import restaurant.order.plates.domain.PlateRepository;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryPlateRepository implements PlateRepository {

    private final HashMap<String, Plate> plates = new HashMap<>();

    @Override
    public void save(Plate plate) {
        this.plates.put(plate.getId().getValue(), plate);
    }

    @Override
    public Optional<Plate> search(PlateId id) {
        return Optional.ofNullable(this.plates.get(id.getValue()));
    }

    @Override
    public Page<Plate> searchAll(int page, int size) {
        return null;
    }
}
