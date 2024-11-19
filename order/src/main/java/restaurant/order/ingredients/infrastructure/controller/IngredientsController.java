package restaurant.order.ingredients.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.order.ingredients.application.create.CreateIngredientCommand;
import restaurant.order.ingredients.application.find.FindIngredientQuery;
import restaurant.order.ingredients.application.find.FindIngredientsQuery;
import restaurant.order.ingredients.application.find.IngredientResponse;
import restaurant.order.ingredients.application.find.ListOfIngredientResponse;
import restaurant.order.ingredients.domain.Ingredient;
import restaurant.order.ingredients.infrastructure.controller.requests.CreateIngredientRequest;
import restaurant.order.shared.Infrastructure.ApiController;
import restaurant.order.shared.domain.bus.command.CommandBus;
import restaurant.order.shared.domain.bus.command.CommandNotRegisteredError;
import restaurant.order.shared.domain.bus.query.QueryBus;
import restaurant.order.shared.domain.bus.query.QueryHandlerExecutionError;

import java.io.Serializable;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientsController extends ApiController {

    @Autowired
    private final CommandBus commandBus;

    @Autowired
    private final QueryBus queryBus;


    public IngredientsController(QueryBus queryBus, CommandBus commandBus) {
        super(queryBus, commandBus);
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @GetMapping("/index")
    public ResponseEntity<Page<Ingredient>> index(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) throws QueryHandlerExecutionError {
        ListOfIngredientResponse response = this.queryBus.ask(new FindIngredientsQuery(page, size));
        return ResponseEntity.ok(response.response());
    }

    @PostMapping("/create")
    public ResponseEntity<String> register(@RequestBody CreateIngredientRequest request) throws CommandNotRegisteredError {
        this.dispatch(new CreateIngredientCommand(request.getId(), request.getName(), request.getQuantity()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String, Serializable>> index(@PathVariable String id)
            throws QueryHandlerExecutionError {

        IngredientResponse response = this.queryBus.ask(new FindIngredientQuery(id));

        Ingredient ingredient = response.getIngredient();

        return ResponseEntity
                .ok()
                .body(
                        new HashMap<String, Serializable>() {
                            {
                                put("id", ingredient.getId().getValue());
                                put("name", ingredient.getName().getValue());
                                put("quantity", ingredient.getQuantity().getValue());
                            }
                        }
                );
    }
}
