package restaurant.order.menu.application.create.find;

import restaurant.order.shared.domain.bus.query.Query;

public class FindPlateQuery implements Query {

    private final String id;

    public FindPlateQuery(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
