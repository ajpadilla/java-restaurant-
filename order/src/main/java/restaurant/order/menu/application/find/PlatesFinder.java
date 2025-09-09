package restaurant.order.menu.application.find;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.PlateRepository;

@Service
public class PlatesFinder {

    private final PlateRepository repository;


    public PlatesFinder(PlateRepository repository) {
        this.repository = repository;
    }

    public Page<PlateResponse> find(int page, int size) {
        return this.repository.searchAll(page, size);
    }
}
