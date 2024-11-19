package restaurant.order.ingredients.application.find;

import restaurant.order.shared.domain.bus.query.Query;

public class FindIngredientQuery  implements Query {

    private final String id;

    public FindIngredientQuery(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
