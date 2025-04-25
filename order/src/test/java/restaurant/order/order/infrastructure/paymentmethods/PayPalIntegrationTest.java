package restaurant.order.order.infrastructure.paymentmethods;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PayPalIntegrationTest {

    @Autowired
    private PayPalPaymentService payPalPaymentService;

    @Test
    void shouldCreatePaymentInSandbox() throws PayPalRESTException {
        // Arrange
        /*Double total = 50.0;
        String currency = "USD";
        String description = "Integration Test Payment";

        // Act
        Payment payment = payPalPaymentService.createPayment(total, currency, description);

        // Assert
        assertNotNull(payment);
        assertEquals("approved", payment.getState()); // Asegúrate de que el estado sea "approved" en el sandbox*/
    }

}
