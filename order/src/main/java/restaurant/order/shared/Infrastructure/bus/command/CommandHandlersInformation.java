package restaurant.order.shared.Infrastructure.bus.command;

import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.bus.command.Command;
import restaurant.order.shared.domain.bus.command.CommandHandler;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public final class CommandHandlersInformation {

    private final HashMap<Class<? extends Command>, CommandHandler<?>> indexedCommandHandlers = new HashMap<>();

    public CommandHandlersInformation(List<CommandHandler<?>> handlers) {
        for (CommandHandler<?> handler : handlers) {
            Class<? extends Command> commandClass = extractCommandClass(handler);
            indexedCommandHandlers.put(commandClass, handler);
        }
    }

    private Class<? extends Command> extractCommandClass(CommandHandler<?> handler) {
        ParameterizedType paramType = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
        return (Class<? extends Command>) paramType.getActualTypeArguments()[0];
    }

    public CommandHandler<?> search(Class<? extends Command> commandClass) throws CommandNotRegisteredError {
        CommandHandler<?> handler = indexedCommandHandlers.get(commandClass);
        if (handler == null) {
            throw new CommandNotRegisteredError(commandClass);
        }
        return handler;
    }
}
