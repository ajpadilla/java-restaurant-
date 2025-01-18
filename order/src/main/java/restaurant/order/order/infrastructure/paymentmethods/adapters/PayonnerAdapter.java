package restaurant.order.order.infrastructure.paymentmethods.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.order.order.domain.PaymentOrderProcessor;
import restaurant.order.order.infrastructure.paymentmethods.PayoneerPaymentService;
import restaurant.order.order.infrastructure.paymentmethods.request.PaymentRequest;


@Service
public class PayonnerAdapter implements PaymentOrderProcessor {

    @Autowired
    private final PayoneerPaymentService payoneerPayment;

    public PayonnerAdapter(PayoneerPaymentService payoneerPayment) {
        this.payoneerPayment = payoneerPayment;
    }

    @Override
    public void processPayment(PaymentRequest request) {
        this.payoneerPayment.sendPayment(200);
    }
}
