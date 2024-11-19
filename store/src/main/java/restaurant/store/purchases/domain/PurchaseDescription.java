package restaurant.store.purchases.domain;

import restaurant.store.shared.domain.StringValueObject;

public class PurchaseDescription extends StringValueObject {
    public PurchaseDescription(String value) {
        super(value);
    }

    public PurchaseDescription() {
        super("");
    }

}
