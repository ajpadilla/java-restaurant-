package restaurant.order.order.infrastructure.paymentmethods;


import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {
    public void payWithStripe(double amount) {
        System.out.println("Payment of $" + amount + " processed via Stripe.");
    }

}
