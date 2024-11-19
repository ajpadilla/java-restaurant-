package restaurant.store.purchases.domain;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PurchaseRepository {
    public void save(Purchase purchase);

    public Optional<Purchase> search(PurchaseId id);

    public Optional<Purchase> searchByName(String name);  // Nuevo método

    Page<Purchase> searchAll(int page, int size);
}
