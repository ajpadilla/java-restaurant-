package restaurant.order.shared.Infrastructure.bus.command;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.command.Command;
import restaurant.order.shared.domain.bus.command.CommandBus;
import restaurant.order.shared.domain.bus.command.CommandHandler;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;

import java.util.concurrent.ExecutorService;

@Service
public class AsyncCommandBus implements CommandBus {

    private final CommandHandlersInformation information;
    private final ApplicationContext context;
    private final ExecutorService commandExecutor;

    public AsyncCommandBus(CommandHandlersInformation information,
                           ApplicationContext context,
                           @Qualifier("commandExecutor") ExecutorService commandExecutor) {
        this.information = information;
        this.context = context;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void dispatch(Command command) throws CommandNotRegisteredError {
            Class<? extends CommandHandler> handlerClass = information.search(command.getClass());
            CommandHandler handler = context.getBean(handlerClass);

        commandExecutor.submit(() -> {
            try {
                // simulate heavy processing
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}

            handler.handle(command);
        });
    }
}
