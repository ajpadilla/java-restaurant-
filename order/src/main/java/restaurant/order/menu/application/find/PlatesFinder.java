package restaurant.order.menu.application.find;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.PlateRepository;
import restaurant.order.order.application.find.dto.OrderResponse;
import restaurant.order.shared.cache.Cache;

@Service
public class PlatesFinder {

    private final PlateRepository repository;
    private final Cache cache;

    public PlatesFinder(PlateRepository repository, Cache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public Page<PlateResponse> find(int page, int size) {
        String key = "plates:page=" + page + ":size=" + size;

        Page<PlateResponse> cached = cache.get(
                key,
                new TypeReference<PageImpl<PlateResponse>>() {}
        );

        if (cached != null) return cached;

        Page<PlateResponse> ordersPage = repository.searchAll(page, size);
        cache.put(key, ordersPage, 300);

        return ordersPage;
    }
}
