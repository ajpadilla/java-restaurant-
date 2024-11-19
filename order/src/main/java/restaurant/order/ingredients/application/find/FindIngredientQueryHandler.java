package restaurant.order.ingredients.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.ingredients.domain.IngredientId;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindIngredientQueryHandler implements QueryHandler<FindIngredientQuery, IngredientResponse> {

    private final IngredientFinder finder;

    public FindIngredientQueryHandler(IngredientFinder finder) {
        this.finder = finder;
    }

    @Override
    public IngredientResponse handle(FindIngredientQuery query) {
        return  this.finder.find(new IngredientId(query.getId()));
    }
}
