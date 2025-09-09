package restaurant.order.config;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;

@Component
public class DebeziumListener {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private DebeziumEngine<ChangeEvent<String, String>> engine;

    public DebeziumListener() {
        //executor.execute(this::startDebezium);
    }

    private void startDebezium() {
       /* Properties props = new Properties();
        props.setProperty("name", "postgres-connector");
        props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "5432");
        props.setProperty("database.user", "ajpadilla");
        props.setProperty("database.password", "Heme19234099");
        props.setProperty("database.dbname", "restaurant_kitchen");
        props.setProperty("database.server.id", "1");
        props.setProperty("plugin.name", "pgoutput");
        props.setProperty("slot.name", "debezium_slot");
        props.setProperty("database.history.kafka.bootstrap.servers", "localhost:9092");
        props.setProperty("database.history.kafka.topic", "schema-changes.postgres");

        // Use ChangeEvent<String, String> instead of String
        Consumer<ChangeEvent<String, String>> handler = changeEvent ->
                System.out.println("Received event: " + changeEvent.value());

        engine = DebeziumEngine
                .create(Json.class)
                .using(props)
                .notifying(handler)  // This should now work!
                .build();

        executor.execute(engine);*/
    }

    @PreDestroy
    public void stopDebezium() throws IOException {
     /*   if (engine != null) {
            engine.close();  // Gracefully stop the Debezium engine
        }
        executor.shutdown();  // Shut down the executor gracefully*/
    }
}