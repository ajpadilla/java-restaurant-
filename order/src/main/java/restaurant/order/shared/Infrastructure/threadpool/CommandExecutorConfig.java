package restaurant.order.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class CommandExecutorConfig {

    @Bean(name = "commandExecutor")
    public ExecutorService commandExecutor() {
        return new ThreadPoolExecutor(
                10, // core pool size
                10, // max pool size
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100), // queue capacity
                new ThreadPoolExecutor.CallerRunsPolicy() // backpressure strategy
        );
    }
}
