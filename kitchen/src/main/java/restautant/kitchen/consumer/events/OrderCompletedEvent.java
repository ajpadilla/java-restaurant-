package restautant.kitchen.consumer.events;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderCompletedEvent {
    private String orderId;
}
