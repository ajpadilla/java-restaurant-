package restautant.kitchen.plate.domain;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PlateRepository {

    void save(Plate plate);

    Optional<Plate> search(PlateId id);

    Page<Plate> searchAll(int page, int size);

}
