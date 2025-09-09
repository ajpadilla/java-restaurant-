package restaurant.order.menu.infrastructure.persistence;

import org.springframework.data.domain.Page;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.menu.domain.PlateRepository;

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
    public Page<PlateResponse> searchAll(int page, int size) {
        return null;
    }
}
