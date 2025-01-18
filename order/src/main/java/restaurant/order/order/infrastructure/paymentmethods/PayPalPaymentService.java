package restaurant.order.order.infrastructure.paymentmethods;

import com.paypal.api.payments.Amount;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalPayment {
    private final APIContext apiContext;


    public PayPalPayment(APIContext apiContext) {
        this.apiContext = apiContext;
    }

    public Payment createPayment(Double total, String currency, String description)  throws PayPalRESTException{

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        return payment.create(apiContext); // Llamada al servidor PayPal

    }
}
