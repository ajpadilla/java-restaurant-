package restautant.kitchen.order.infrastructure.paymentmethods.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restautant.kitchen.order.domain.PaymentOrderProcessor;
import restautant.kitchen.order.infrastructure.paymentmethods.PayPalPayment;

@Service
public class PaypalAdapter implements PaymentOrderProcessor {

    @Autowired
    private final PayPalPayment payPalPayment;

    public PaypalAdapter(PayPalPayment payPalPayment) {
        this.payPalPayment = payPalPayment;
    }

    @Override
    public void processPayment(double amount) {
        this.payPalPayment.makePayment(100);
    }
}
