package restautant.kitchen.order.infrastructure.controller;

import java.util.List;


public class CreateOrderRequest {

    private String orderId;
    private List<PlateRequest> plates;

    // Constructor sin parámetros
    public CreateOrderRequest() {}

    // Constructor con parámetros
    public CreateOrderRequest(String orderId, List<PlateRequest> plates) {
        this.orderId = orderId;
        this.plates = plates;
    }

    // Getters y Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PlateRequest> getPlates() {
        return plates;
    }

    public void setPlates(List<PlateRequest> plates) {
        this.plates = plates;
    }

    // toString() (opcional)
    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "orderId='" + orderId + '\'' +
                ", plates=" + plates +
                '}';
    }
}
