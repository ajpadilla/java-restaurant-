package restaurant.order.menu.application.find;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.PlateRepository;
import restaurant.order.order.application.find.dto.OrderResponse;
import restaurant.order.shared.cache.Cache;

import java.util.List;

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

        // Try to get list from cache
        List<PlateResponse> cachedList = cache.get(
                key,
                new TypeReference<List<PlateResponse>>() {}
        );

        if (cachedList != null) {
            return new PageImpl<>(cachedList, PageRequest.of(page, size), cachedList.size());
        }

        // If cache miss
        Page<PlateResponse> platesPage = repository.searchAll(page, size);

        // Cache only the content list
        cache.put(key, platesPage.getContent(), 300);

        return platesPage;
    }
}
