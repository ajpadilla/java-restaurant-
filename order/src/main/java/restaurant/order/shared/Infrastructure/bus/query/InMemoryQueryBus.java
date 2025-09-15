package restaurant.order.shared.Infrastructure.bus.query;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.query.*;

import java.util.List;

@Service
public final class InMemoryQueryBus implements QueryBus {
    private final QueryHandlersInformation information;

    public InMemoryQueryBus(List<QueryHandler<?, ?>> handlers) {
        this.information = new QueryHandlersInformation(handlers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R ask(Query query) throws QueryHandlerExecutionError {
        try {
            QueryHandler<Query, R> handler = (QueryHandler<Query, R>) information.search(query.getClass());
            return handler.handle(query);
        } catch (Throwable error) {
            throw new QueryHandlerExecutionError(error);
        }
    }

}
