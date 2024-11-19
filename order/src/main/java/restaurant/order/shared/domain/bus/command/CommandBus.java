package restaurant.order.shared.domain.bus.command;

public interface CommandBus {
    void dispatch(Command command) throws CommandNotRegisteredError;
}