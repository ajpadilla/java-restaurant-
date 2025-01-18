package restaurant.order.order.infrastructure.paymentmethods.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restautant.kitchen.order.domain.PaymentOrderProcessor;
import restautant.kitchen.order.infrastructure.paymentmethods.StripePayment;

@Service
public class StripeAdapter implements PaymentOrderProcessor {


    private final StripePayment stripePayment;

    public StripeAdapter(StripePayment stripePayment) {
        this.stripePayment = stripePayment;
    }

    @Override
    public void processPayment(double amount) {
        this.stripePayment.payWithStripe(100);
    }
}
