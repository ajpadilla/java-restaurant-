package restaurant.order.order.application.find.dto;

import restaurant.order.menu.application.find.dto.IngredientResponse;
import restaurant.order.shared.domain.bus.query.Response;

import java.util.List;

public record OrderResponse(String id, List<PlateResponse> platesIds) implements Response { }
