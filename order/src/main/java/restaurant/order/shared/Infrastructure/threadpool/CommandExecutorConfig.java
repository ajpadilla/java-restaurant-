package restaurant.order.shared.Infrastructure.threadpool;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class CommandExecutorConfig {
    @Bean
    public ExecutorService commandExecutor(MeterRegistry registry) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10, 20,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // register metrics for the executor
        return ExecutorServiceMetrics.monitor(
                registry,
                executor,
                "command.executor",
                Tags.of("type", "command")
        );
    }
}
