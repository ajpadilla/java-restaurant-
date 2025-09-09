package restaurant.order.menu.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.order.menu.infrastructure.controller.request.CreatePlateRequest;
import restaurant.order.menu.application.create.CreatePlateCommand;
import restaurant.order.menu.application.find.FindPlateQuery;
import restaurant.order.menu.application.find.FindPlatesQuery;
import restaurant.order.menu.application.find.dto.PlateResponse;
import restaurant.order.menu.domain.Plate;
import restaurant.order.shared.Infrastructure.ApiController;
import restaurant.order.shared.domain.bus.command.CommandBus;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;
import restaurant.order.shared.domain.bus.query.QueryBus;
import restaurant.order.shared.domain.bus.query.QueryHandlerExecutionError;

import java.util.List;

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
    public ResponseEntity<Page<PlateResponse>> index(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) throws QueryHandlerExecutionError {
        Page<PlateResponse> response = queryBus.ask(new FindPlatesQuery(page, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<String> register(@RequestBody CreatePlateRequest request) throws CommandNotRegisteredError  {
        List<CreatePlateCommand.PlateIngredientCommand> ingredientsCommand = request.getIngredients().stream()
                .map(i -> new CreatePlateCommand.PlateIngredientCommand(i.getIngredientId(), i.getIngredientName(), i.getRequiredQuantity()))
                .toList();

        this.dispatch(new CreatePlateCommand(request.getId(), request.getName(), ingredientsCommand));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlateResponse> index(@PathVariable String id) throws QueryHandlerExecutionError {
        PlateResponse response = this.queryBus.ask(new FindPlateQuery(id));
        return ResponseEntity.ok(response);
    }
}
