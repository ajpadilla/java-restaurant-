package restautant.kitchen.order.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderStatusService {
    private final RedisPort redisService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderStatusService(RedisPort redisService, OrderRepository orderRepository) {
        this.redisService = redisService;
        this.orderRepository = orderRepository;
    }

    // Verificar el estado de la orden en la base de datos
    public Optional<OrderStatus> getOrderStatusFromDatabase(OrderId orderId) {
        Optional<Order> order = this.orderRepository.search(orderId);
        return order.map(Order::getStatus); // Si existe, retorna el status, si no, retorna Optional.empty()
    }
}
