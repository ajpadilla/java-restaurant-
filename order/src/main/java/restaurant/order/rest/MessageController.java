package restaurant.order.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.order.kafka.KafkaJsonProducer;
import restaurant.order.kafka.KafkaProducer;
import restaurant.order.kafka.KafkaProducerService;
import restaurant.order.kafka.payload.Student;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducer kafkaProducer;
    private final KafkaJsonProducer kafkaJsonProducer;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping
    public ResponseEntity<String> sendMessage(
            @RequestBody String message
    ) {
        //kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message queued successfully");
    }

    @PostMapping("/json")
    public ResponseEntity<String> sendJsonMessage(
            @RequestBody Student message
    ) throws JsonProcessingException {
        System.out.println("Received JSON message: " + message.toString());  // Esto imprime el objeto en la consola
        //kafkaJsonProducer.sendMessage(message);

        kafkaProducerService.sendUser(message);
        return ResponseEntity.ok("Message queued successfully as JSON");
    }
}
