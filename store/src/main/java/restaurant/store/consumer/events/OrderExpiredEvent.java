package restaurant.store.consumer.events;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderExpiredEvent {
    String orderId;
}
