package restaurant.order.order.application.find;

import org.springframework.data.domain.Page;
import restaurant.order.order.domain.Order;
import restaurant.order.shared.domain.bus.query.Response;

public class ListOfOrdersResponse implements Response {
    private final Page<Order> orders;

    public ListOfOrdersResponse(Page<Order> orders) {
        this.orders = orders;
    }

    public Page<Order> response() {
        return orders;
    }

}
