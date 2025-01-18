package restautant.kitchen.order.infrastructure.paymentmethods;


import org.springframework.stereotype.Service;

@Service
public class PayoneerPayment {
    public void sendPayment(double amount) {
        System.out.println("Payment of $" + amount + " processed via Payoneer.");
    }
}
