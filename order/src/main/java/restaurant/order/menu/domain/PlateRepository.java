package restaurant.order.menu.domain;

import org.springframework.data.domain.Page;
import restaurant.order.menu.application.find.dto.PlateResponse;

import java.util.Optional;

public interface PlateRepository {

    void save(Plate plate);

    Optional<Plate> search(PlateId id);

    Page<PlateResponse> searchAll(int page, int size);

}
