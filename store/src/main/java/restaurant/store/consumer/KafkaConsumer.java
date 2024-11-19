package restaurant.store.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class KafkaConsumer {

    /*@KafkaListener(topics = "alibou", groupId = "myGroup")
    public void consumeMsg(String msg) {
        log.info(String.format("Consuming the message from alibou Topic:: %s", msg));
    }*/


  /* @KafkaListener(topics = "alibou2", groupId = "myGroup")
    public void consumeJsonMsg(Student student) {
       log.info(format("Consuming the message from alibou Topic:: %s", student.toString()));
    }*/

  /*  @KafkaListener(topics = "alibou2", groupId = "myGroup")
    public void consume(String userJson) {
        System.out.println("Received User: " + userJson);
    }*/

    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "alibou2", groupId = "myGroup")
    public void consume(String userJson) {
        try {
            // Deserializar el JSON a un objeto Student
            Student student = objectMapper.readValue(userJson, Student.class);

            // Acceder a los valores del objeto Student
            System.out.println("Received User: " + student.getFirstname() + ", Email: " + student.getLastname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
