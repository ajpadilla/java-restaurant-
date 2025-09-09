package restaurant.order.menu.application.find.dto;

import restaurant.order.menu.domain.Plate;
import restaurant.order.shared.domain.bus.query.Response;

import java.util.List;

public record PlateResponse(String id, String name, List<IngredientResponse> ingredients) implements Response { }
