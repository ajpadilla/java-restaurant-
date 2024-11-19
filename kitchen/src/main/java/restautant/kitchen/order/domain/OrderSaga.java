package restautant.kitchen.order.domain;

import restautant.kitchen.ingredient.domain.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class OrderSaga {
    private final Order order;
    private final Map<Ingredient, Integer> ingredientStates = new HashMap<>(); // almacena la cantidad recibida de cada ingrediente

    public OrderSaga(Order order) {
        this.order = order;
        // Inicializa los ingredientes con cantidad recibida = 0
        order.getPlate().getIngredients().forEach(ingredient -> ingredientStates.put(ingredient, 0));
    }

    // Actualiza la cantidad recibida de un ingrediente
    public void updateIngredient(Ingredient ingredient, int receivedQuantity) {
        // Verifica si el ingrediente ya existe en el mapa
        if (ingredientStates.containsKey(ingredient)) {
            ingredientStates.put(ingredient, receivedQuantity);
        } else {
            throw new IllegalArgumentException("Ingredient not found in the order");
        }
    }

    // Verifica si un ingrediente específico está completo
    public boolean isIngredientComplete(Ingredient ingredient) {
        Integer receivedQuantity = ingredientStates.get(ingredient);
        if (receivedQuantity != null) {
            return receivedQuantity >= ingredient.getQuantity().getValue(); // Usa el método getValue de IngredientQuantity
        }
        return false;
    }

    // Verifica si todos los ingredientes del pedido están completos
    public boolean isComplete() {
        return ingredientStates.entrySet().stream()
                .allMatch(entry -> entry.getValue() >= entry.getKey().getQuantity().getValue());
    }

    // Retorna el pedido asociado a la saga
    public Order getOrder() {
        return this.order;
    }

}
