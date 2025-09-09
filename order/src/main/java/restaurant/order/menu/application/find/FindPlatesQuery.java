package restaurant.order.menu.application.create.find;

import restaurant.order.shared.domain.bus.query.Query;
public final class FindPlatesQuery implements Query  {

    private final int page;

    private final int size;

    public FindPlatesQuery(int page, int size) {
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
