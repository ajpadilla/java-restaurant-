package restaurant.order.order.application.find;

import restaurant.order.shared.domain.bus.query.Query;

public final class FindOrdersQuery implements Query {

    private final int page;

    private final int size;


    public FindOrdersQuery(int page, int size) {
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
