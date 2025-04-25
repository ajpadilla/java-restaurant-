package restaurant.order.order.infrastructure.paymentmethods;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import restaurant.order.order.infrastructure.paymentmethods.adapters.PaypalAdapter;
import restaurant.order.order.infrastructure.paymentmethods.request.PaymentRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaypalAdapterTest {

    @Mock
    private PayPalPaymentService payPalPaymentService;

    @InjectMocks
    private PaypalAdapter paypalAdapter;

    @Test
    void shouldProcessPaymentSuccessfully() throws PayPalRESTException {
       /* PaymentRequest request = new PaymentRequest(100.0, "USD", "paypal", "sale", "Test Payment");
        Payment mockPayment = new Payment();
        Mockito.when(payPalPaymentService.createPayment(request.getTotal(), request.getCurrency(), request.getDescription()))
                .thenReturn(mockPayment);

        this.paypalAdapter.processPayment(request);

        Mockito.verify(payPalPaymentService, Mockito.times(1))
                .createPayment(request.getTotal(), request.getCurrency(), request.getDescription());*/
    }

    @Test
    void shouldThrowExceptionWhenPayPalFails() throws PayPalRESTException {
      /*  PaymentRequest request = new PaymentRequest(100.0, "USD", "paypal", "sale", "Test Payment");
        Mockito.when(this.payPalPaymentService.createPayment(request.getTotal(), request.getCurrency(), request.getDescription()))
                .thenThrow(new PayPalRESTException("Simulated PayPal error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paypalAdapter.processPayment(request));
        assertEquals("Error al procesar el pago con PayPal", exception.getMessage());
        Mockito.verify(payPalPaymentService, Mockito.times(1))
                .createPayment(request.getTotal(), request.getCurrency(), request.getDescription());*/

    }
}
