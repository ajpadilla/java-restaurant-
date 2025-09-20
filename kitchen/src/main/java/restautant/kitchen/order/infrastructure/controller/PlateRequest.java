package restautant.kitchen.order.infrastructure.controller;

import java.util.List;


public class PlateRequest {
    private String plateId;
    private String plateName;
    private List<IngredientRequest> ingredients;

    // Constructor sin parámetros
    public PlateRequest() {}

    // Constructor con parámetros
    public PlateRequest(String plateId, String plateName, List<IngredientRequest> ingredients) {
        this.plateId = plateId;
        this.plateName = plateName;
        this.ingredients = ingredients;
    }

    // Getters y Setters
    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public List<IngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }

    // toString() (opcional)
    @Override
    public String toString() {
        return "PlateRequest{" +
                "plateId='" + plateId + '\'' +
                ", plateName='" + plateName + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
