package restaurant.order.kafka;

import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaJsonProducer {
   /* private final KafkaTemplate<String, Student> kafkaTemplate;

    public void sendMessage(Student student) {

        Message<Student> message = MessageBuilder
                .withPayload(student)
                .setHeader(KafkaHeaders.TOPIC, "alibou2")
                .build();

        kafkaTemplate.send(message);




    }*/
}