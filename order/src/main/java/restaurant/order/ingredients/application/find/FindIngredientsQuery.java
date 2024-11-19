package restaurant.order.ingredients.application.find;

import restaurant.order.shared.domain.bus.query.Query;

public final class FindIngredientsQuery implements Query {

    private final int page;

    private final int size;


    public FindIngredientsQuery(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
