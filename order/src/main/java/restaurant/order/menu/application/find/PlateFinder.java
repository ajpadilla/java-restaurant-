package restaurant.order.menu.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.IngredientResponse;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.menu.domain.exception.PlateNotFoundException;
import restaurant.order.menu.domain.PlateRepository;

import java.util.List;

@Service
public class PlateFinder {

    private final PlateRepository repository;


    public PlateFinder(PlateRepository repository) {
        this.repository = repository;
    }

    public PlateResponse find(PlateId id) {
        Plate plate = repository.search(id)
                .orElseThrow(() -> new PlateNotFoundException("Plate not found: " + id.getValue()));

        List<IngredientResponse> ingredients = plate.getIngredients().stream()
                .map(i -> new IngredientResponse(
                        i.getId().getValue(),
                        i.getName().getValue(),
                        i.getQuantity().getValue()))
                .toList();

        return new PlateResponse(plate.getId().getValue(), plate.getName().getValue(), ingredients);
    }
}
