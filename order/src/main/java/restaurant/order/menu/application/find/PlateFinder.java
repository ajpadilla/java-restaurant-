package restaurant.order.menu.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.IngredientResponse;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.Plate;
import restaurant.order.menu.domain.PlateId;
import restaurant.order.menu.domain.exception.PlateNotFoundException;
import restaurant.order.menu.domain.PlateRepository;
import restaurant.order.shared.cache.Cache;

import java.util.List;

@Service
public class PlateFinder {
    private final PlateRepository repository;
    private final Cache cache;

    public PlateFinder(PlateRepository repository, Cache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public PlateResponse find(PlateId id) {
        String key = "plate:" + id.getValue();

        PlateResponse cached = cache.get(key, PlateResponse.class);
        if (cached != null) return cached;


        Plate plate = repository.search(id)
                .orElseThrow(() -> new PlateNotFoundException("Plate not found: " + id.getValue()));

        List<IngredientResponse> ingredients = plate.getIngredients().stream()
                .map(i -> new IngredientResponse(
                        i.getId().getValue(),
                        i.getName().getValue(),
                        i.getQuantity().getValue()))
                .toList();

        PlateResponse response = new PlateResponse(plate.getId().getValue(), plate.getName().getValue(), ingredients);

        cache.put(key, response, 300); // cache for 5 minutes

        return response;
    }
}
