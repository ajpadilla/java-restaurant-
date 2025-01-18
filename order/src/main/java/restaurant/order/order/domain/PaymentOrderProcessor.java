package restaurant.order.order.domain;

import restaurant.order.order.infrastructure.paymentmethods.request.PaymentRequest;

public interface PaymentOrderProcessor {
    void processPayment(PaymentRequest request);
}
