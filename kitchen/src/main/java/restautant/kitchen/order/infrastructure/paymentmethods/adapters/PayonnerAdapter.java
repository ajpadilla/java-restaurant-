package restautant.kitchen.order.infrastructure.paymentmethods.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restautant.kitchen.order.domain.PaymentOrderProcessor;
import restautant.kitchen.order.infrastructure.paymentmethods.PayPalPayment;
import restautant.kitchen.order.infrastructure.paymentmethods.PayoneerPayment;

@Service
public class PayonnerAdapter implements PaymentOrderProcessor {

    @Autowired
    private final PayoneerPayment payoneerPayment;

    public PayonnerAdapter(PayoneerPayment payoneerPayment) {
        this.payoneerPayment = payoneerPayment;
    }

    @Override
    public void processPayment(double amount) {
        this.payoneerPayment.sendPayment(200);
    }
}
