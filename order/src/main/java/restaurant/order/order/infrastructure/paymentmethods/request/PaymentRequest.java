package restaurant.order.order.infrastructure.paymentmethods.request;

public class PaymentRequest {

    private Double total;
    private String currency;
    private String method;
    private String intent;
    private String description;

    // Constructor
    public PaymentRequest(Double total, String currency, String method, String intent, String description) {
        this.total = total;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.description = description;
    }

    // Getters
    public Double getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMethod() {
        return method;
    }

    public String getIntent() {
        return intent;
    }

    public String getDescription() {
        return description;
    }

    // Setters (Opcionales, según si necesitas mutabilidad)
    public void setTotal(Double total) {
        this.total = total;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
