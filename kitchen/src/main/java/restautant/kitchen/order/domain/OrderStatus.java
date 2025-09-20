package restautant.kitchen.order.domain;

public enum OrderStatus {

    PENDING,     // La orden acaba de llegar a la cocina y está pendiente de preparación
    COOKING,     // La orden está siendo preparada
    READY,       // La orden está lista para ser entregada
    DELIVERED,   // La orden fue entregada al cliente o enviada a su destino
    CANCELLED    // La orden fue cancelada por alguna razón

}
