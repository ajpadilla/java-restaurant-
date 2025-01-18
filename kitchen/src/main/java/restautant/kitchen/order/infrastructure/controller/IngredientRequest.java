package restautant.kitchen.order.infrastructure;


public class IngredientRequest {

    private String id;
    private String name;
    private int quantity;

    // Constructor sin parámetros
    public IngredientRequest() {}

    // Constructor con parámetros
    public IngredientRequest(String ingredientId, String ingredientName, int quantity) {
        this.id = ingredientId;
        this.name = ingredientName;
        this.quantity = quantity;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString() (opcional)
    @Override
    public String toString() {
        return "IngredientRequest{" +
                "ingredientId='" + id + '\'' +
                ", ingredientName='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }

}
