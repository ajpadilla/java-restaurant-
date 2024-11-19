package restaurant.store.consumer.events;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderCompletedEvent {
    private String orderId;
}
