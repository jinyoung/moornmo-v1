package moornmov.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import moornmov.query.*;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EmployeeSearchQueryController {

    private final QueryGateway queryGateway;

    private final ReactorQueryGateway reactorQueryGateway;

    public EmployeeSearchQueryController(
        QueryGateway queryGateway,
        ReactorQueryGateway reactorQueryGateway
    ) {
        this.queryGateway = queryGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @GetMapping("/employees")
    public CompletableFuture findAll(EmployeeSearchQuery query) {
        return queryGateway
            .query(
                query,
                ResponseTypes.multipleInstancesOf(EmployeeReadModel.class)
            )
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<EmployeeReadModel>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<EmployeeReadModel> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/employees/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        EmployeeSearchSingleQuery query = new EmployeeSearchSingleQuery();
        query.setId(id);

        return queryGateway
            .query(
                query,
                ResponseTypes.optionalInstanceOf(EmployeeReadModel.class)
            )
            .thenApply(resource -> {
                if (!resource.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(
                    hateoas(resource.get()),
                    HttpStatus.OK
                );
            })
            .exceptionally(ex -> {
                throw new RuntimeException(ex);
            });
    }

    EntityModel<EmployeeReadModel> hateoas(EmployeeReadModel resource) {
        EntityModel<EmployeeReadModel> model = EntityModel.of(resource);

        model.add(Link.of("/employees/" + resource.getId()).withSelfRel());

        model.add(
            Link
                .of("/employees/" + resource.getId() + "/events")
                .withRel("events")
        );

        return model;
    }

    @MessageMapping("employees.all")
    public Flux<EmployeeReadModel> subscribeAll() {
        return reactorQueryGateway.subscriptionQueryMany(
            new EmployeeSearchQuery(),
            EmployeeReadModel.class
        );
    }

    @MessageMapping("employees.{id}.get")
    public Flux<EmployeeReadModel> subscribeSingle(
        @DestinationVariable String id
    ) {
        EmployeeSearchSingleQuery query = new EmployeeSearchSingleQuery();
        query.setId(id);

        return reactorQueryGateway.subscriptionQuery(
            query,
            EmployeeReadModel.class
        );
    }
}
