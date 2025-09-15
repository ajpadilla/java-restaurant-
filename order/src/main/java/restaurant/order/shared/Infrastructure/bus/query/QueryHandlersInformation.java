package restaurant.order.shared.Infrastructure.bus.query;


import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import restaurant.order.menu.application.find.*;
import restaurant.order.order.application.find.FindOrdersQueryHandler;
import restaurant.order.shared.domain.bus.command.CommandHandler;
import restaurant.order.shared.domain.bus.query.Query;
import restaurant.order.shared.domain.bus.query.QueryHandler;
import restaurant.order.shared.domain.bus.query.QueryNotRegisteredError;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public final class QueryHandlersInformation {

    private final HashMap<Class<? extends Query>, QueryHandler<?, ?>> indexedQueryHandlers = new HashMap<>();

    public QueryHandlersInformation(List<QueryHandler<?, ?>> handlers) {
        for (QueryHandler<?, ?> handler : handlers) {
            Class<? extends Query> queryClass = extractQueryClass(handler);
            indexedQueryHandlers.put(queryClass, handler);
        }

        // Debug log
        indexedQueryHandlers.forEach((query, handler) ->
                System.out.println("Registered query: " + query.getSimpleName() + " -> " + handler.getClass().getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    public <Q extends Query, R> QueryHandler<Q, R> search(Class<Q> queryClass) throws QueryNotRegisteredError {
        QueryHandler<?, ?> handler = indexedQueryHandlers.get(queryClass);
        if (handler == null) {
            throw new QueryNotRegisteredError(queryClass);
        }
        return (QueryHandler<Q, R>) handler;
    }

    private Class<? extends Query> extractQueryClass(QueryHandler<?, ?> handler) {
        // Use reflection to get generic type
        return (Class<? extends Query>) ((ParameterizedType) handler.getClass()
                .getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
    }
}