package restaurant.order.plates.application.find;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import restaurant.order.plates.domain.Plate;
import restaurant.order.plates.domain.PlateRepository;

@Service
public class PlatesFinder {

    private final PlateRepository repository;


    public PlatesFinder(PlateRepository repository) {
        this.repository = repository;
    }

    public ListOfPlateResponse find(int page, int size) {
        Page<Plate> plates = this.repository.searchAll(page, size);
        return new ListOfPlateResponse(plates);
    }
}
