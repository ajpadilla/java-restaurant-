package restaurant.order.order.infrastructure.paymentmethods.adapters;

import org.springframework.stereotype.Service;
import restaurant.order.order.domain.PaymentOrderProcessor;
import restaurant.order.order.infrastructure.paymentmethods.StripePaymentService;
import restaurant.order.order.infrastructure.paymentmethods.request.PaymentRequest;

@Service
public class StripeAdapter implements PaymentOrderProcessor {


    private final StripePaymentService stripePayment;

    public StripeAdapter(StripePaymentService stripePayment) {
        this.stripePayment = stripePayment;
    }

    @Override
    public void processPayment(PaymentRequest request) {
        this.stripePayment.payWithStripe(100);
    }
}
