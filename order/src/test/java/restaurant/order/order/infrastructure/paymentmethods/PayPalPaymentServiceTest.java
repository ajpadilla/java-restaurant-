package restaurant.order.order.infrastructure.paymentmethods;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PayPalPaymentServiceTest {

    @Mock
    private APIContext apiContext;

    private PayPalPaymentService payPalPaymentService;

    @BeforeEach
    void setUp() {
        payPalPaymentService = new PayPalPaymentService(apiContext);
    }
    @Test
    void shouldCreatePaymentSuccessfully() throws PayPalRESTException {
        // Arrange
        /*Double total = 100.0;
        String currency = "USD";
        String description = "Test Payment";

        Payment mockPayment = Mockito.mock(Payment.class);

        // Spy para la clase que probamos
        PayPalPaymentService spyService = Mockito.spy(payPalPaymentService);
        Mockito.doReturn(mockPayment).when(spyService).createPaymentInstance();
        Mockito.when(mockPayment.create(apiContext)).thenReturn(mockPayment);

        // Act
        Payment payment = spyService.createPayment(total, currency, description);

        // Assert
        assertNotNull(payment);*/
    }
}
