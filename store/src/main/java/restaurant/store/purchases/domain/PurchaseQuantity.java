package restaurant.store.purchases.domain;

import restaurant.store.shared.domain.IntValueObject;

public class PurchaseQuantity extends IntValueObject {
    public PurchaseQuantity(Integer value) {
        super(value);
    }

    public PurchaseQuantity() {
        super(null);
    }
}
