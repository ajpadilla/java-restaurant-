package restaurant.store.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {

    @Bean
    public NewTopic kitchenTopic() {
        /*return TopicBuilder
                .name("alibou")
                .build();*/

        return TopicBuilder
                .name("alibou2")
                .build();
    }

}
