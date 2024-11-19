package restaurant.order.ingredients.application.find;

import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.query.QueryHandler;

@Service
public class FindIngredientsQueryHandler implements QueryHandler<FindIngredientsQuery, ListOfIngredientResponse> {

    private final IngredientsFinder finder;

    public FindIngredientsQueryHandler(IngredientsFinder finder) {
        this.finder = finder;
    }

    @Override
    public ListOfIngredientResponse handle(FindIngredientsQuery query) {
        return this.finder.find(query.getPage(), query.getSize());
    }
}
