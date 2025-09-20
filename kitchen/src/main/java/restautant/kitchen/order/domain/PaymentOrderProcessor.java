package restautant.kitchen.order.domain;

public interface PaymentOrderProcessor {
    void processPayment(double amount);
}
