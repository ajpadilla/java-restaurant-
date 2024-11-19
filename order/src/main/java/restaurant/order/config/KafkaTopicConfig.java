package restaurant.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic kitchenTopic() {
       /* return TopicBuilder
                .name("alibou")
                .build();*/

        return TopicBuilder
                .name("alibou2")
                .build();
    }

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder
                .name("orderCreatedTopic")
                .build();
    }

}
