package restaurant.order.shared.Infrastructure.bus.query;


import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.query.Query;
import restaurant.order.shared.domain.bus.query.QueryHandler;
import restaurant.order.shared.domain.bus.query.QueryNotRegisteredError;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Set;


@Service
public final class QueryHandlersInformation {

    private final HashMap<Class<? extends Query>, Class<? extends QueryHandler<?, ?>>> indexedQueryHandlers;

    public QueryHandlersInformation() {
        Reflections reflections = new Reflections("restaurant.order");
        Set<Class<? extends QueryHandler>> classes = reflections.getSubTypesOf(QueryHandler.class);

        this.indexedQueryHandlers = formatHandlers(classes);
    }

    /**
     * Search for the QueryHandler class associated with a given Query class.
     */
    public Class<? extends QueryHandler<?, ?>> search(Class<? extends Query> queryClass) throws QueryNotRegisteredError {
        Class<? extends QueryHandler<?, ?>> queryHandlerClass = indexedQueryHandlers.get(queryClass);

        if (queryHandlerClass == null) {
            throw new QueryNotRegisteredError(queryClass);
        }

        return queryHandlerClass;
    }

    /**
     * Builds a map of Query -> QueryHandler class.
     */
    @SuppressWarnings("unchecked")
    private HashMap<Class<? extends Query>, Class<? extends QueryHandler<?, ?>>> formatHandlers(
            Set<Class<? extends QueryHandler>> queryHandlers
    ) {
        HashMap<Class<? extends Query>, Class<? extends QueryHandler<?, ?>>> handlers = new HashMap<>();

        for (Class<? extends QueryHandler> handler : queryHandlers) {
            // Extract the first generic interface implemented by the handler
            ParameterizedType paramType = (ParameterizedType) handler.getGenericInterfaces()[0];

            // The first type argument is the Query class
            Class<? extends Query> queryClass = (Class<? extends Query>) paramType.getActualTypeArguments()[0];

            // Store with proper generic parameterization
            handlers.put(queryClass, (Class<? extends QueryHandler<?, ?>>) handler);
        }

        return handlers;
    }
}