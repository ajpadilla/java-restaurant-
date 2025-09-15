package restaurant.order.shared.Infrastructure.bus.command;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.command.Command;
import restaurant.order.shared.domain.bus.command.CommandBus;
import restaurant.order.shared.domain.bus.command.CommandHandler;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;
/*
@Service
public final class InMemoryCommandBus implements CommandBus {
    private final CommandHandlersInformation information;
    private final ApplicationContext context;

    public InMemoryCommandBus(CommandHandlersInformation information, ApplicationContext context) {
        this.information = information;
        this.context     = context;
    }

    @Override
    public void dispatch(Command command) throws CommandNotRegisteredError {
        Class<? extends CommandHandler> commandHandlerClass = information.search(command.getClass());

        CommandHandler handler = context.getBean(commandHandlerClass);

        handler.handle(command);
    }
}*/