package restaurant.order.menu.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.order.menu.infrastructure.controller.request.CreatePlateRequest;
import restaurant.order.plates.application.create.CreatePlateCommand;
import restaurant.order.plates.application.find.FindPlateQuery;
import restaurant.order.plates.application.find.FindPlatesQuery;
import restaurant.order.plates.application.find.ListOfPlateResponse;
import restaurant.order.plates.application.find.PlateResponse;
import restaurant.order.plates.domain.Plate;
import restaurant.order.shared.Infrastructure.ApiController;
import restaurant.order.shared.domain.bus.command.CommandBus;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;
import restaurant.order.shared.domain.bus.query.QueryBus;
import restaurant.order.shared.domain.bus.query.QueryHandlerExecutionError;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/plates")
public class PlateController extends ApiController {

    @Autowired
    private final CommandBus commandBus;

    @Autowired
    private final QueryBus queryBus;

    public PlateController(QueryBus queryBus, CommandBus commandBus) {
        super(queryBus, commandBus);
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @GetMapping("/index")
    public ResponseEntity<Page<Plate>> index(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) throws QueryHandlerExecutionError {
        ListOfPlateResponse response = this.queryBus.ask(new FindPlatesQuery(page, size));
        return ResponseEntity.ok(response.response());
    }

    @PostMapping("/create")
    public ResponseEntity<String> register(@RequestBody CreatePlateRequest request) throws CommandNotRegisteredError  {
        this.dispatch(new CreatePlateCommand(request.getId(), request.getName(), request.getIngredientsIds()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String, Serializable>> index(@PathVariable String id)
            throws QueryHandlerExecutionError {

        PlateResponse response = this.queryBus.ask(new FindPlateQuery(id));

        Plate plate = response.getPlate();

        HashMap<String, Serializable> responseMap = new HashMap<>();

        responseMap.put("id", plate.getId().getValue());
        responseMap.put("name", plate.getName().getValue());

        List<HashMap<String, Serializable>> ingredientsList = plate.getIngredients().stream()
                .map(ingredient -> {
                    HashMap<String, Serializable> ingredientMap = new HashMap<>();
                    ingredientMap.put("id", ingredient.getId().getValue());
                    ingredientMap.put("name", ingredient.getName().getValue());
                    ingredientMap.put("quantity", ingredient.getQuantity().getValue());
                    return ingredientMap;
                })
                .collect(Collectors.toList());

        responseMap.put("ingredients", (Serializable) ingredientsList);

        return ResponseEntity.ok(responseMap);

    }
}
