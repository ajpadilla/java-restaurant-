package restautant.kitchen.order.infrastructure.paymentmethods;

import org.springframework.stereotype.Service;

@Service
public class PayPalPayment {
    public void makePayment(double amount) {
        System.out.println("Payment of $" + amount + " processed via PayPal.");
    }
}
