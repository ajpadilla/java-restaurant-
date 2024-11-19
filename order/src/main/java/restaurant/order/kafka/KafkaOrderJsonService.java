package restaurant.order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import restaurant.order.shared.domain.Utils;

import java.io.Serializable;
import java.util.HashMap;

@Service
public class KafkaOrderJsonService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaOrderJsonService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(HashMap<String, Serializable> orderMap) {
        String userJson = Utils.jsonEncode(orderMap);
        kafkaTemplate.send("orderCreatedTopic", userJson);
    }
}
