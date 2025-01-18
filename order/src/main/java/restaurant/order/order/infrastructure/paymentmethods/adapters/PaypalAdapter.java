package restaurant.order.order.infrastructure.paymentmethods.adapters;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.order.order.domain.PaymentOrderProcessor;
import restaurant.order.order.infrastructure.paymentmethods.PayPalPaymentService;
import restaurant.order.order.infrastructure.paymentmethods.request.PaymentRequest;

@Service
public class PaypalAdapter implements PaymentOrderProcessor {

    @Autowired
    private final PayPalPaymentService payPalPayment;

    public PaypalAdapter(PayPalPaymentService payPalPayment) {
        this.payPalPayment = payPalPayment;
    }

    @Override
    public void processPayment(PaymentRequest request) {
        try {
            // Intentar crear el pago con PayPal
            Payment payment =  this.payPalPayment.createPayment(request.getTotal(), request.getCurrency(), request.getDescription());
        } catch (PayPalRESTException e) {
            // Manejar la excepción de PayPal
            System.err.println("Error al procesar el pago con PayPal: " + e.getMessage());
            // Puedes registrar un log o incluso lanzar una excepción personalizada si es necesario
            throw new RuntimeException("Error al procesar el pago con PayPal", e);
        }
    }
}
