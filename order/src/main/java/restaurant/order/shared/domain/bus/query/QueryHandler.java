package restaurant.order.shared.domain.bus.query;

public interface QueryHandler <Q extends Query, R>{
    R handle(Q query);
}
