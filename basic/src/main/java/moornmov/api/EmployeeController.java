package moornmov.api;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import moornmov.aggregate.*;
import moornmov.command.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public EmployeeController(
        CommandGateway commandGateway,
        QueryGateway queryGateway
    ) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public CompletableFuture registration(
        @RequestBody RegistrationCommand registrationCommand
    ) throws Exception {
        System.out.println("##### /employee/registration  called #####");

        // send command
        return commandGateway
            .send(registrationCommand)
            .thenApply(id -> {
                EmployeeAggregate resource = new EmployeeAggregate();
                BeanUtils.copyProperties(registrationCommand, resource);

                resource.setId((String) id);

                return new ResponseEntity<>(hateoas(resource), HttpStatus.OK);
            });
    }

    @Autowired
    EventStore eventStore;

    @GetMapping(value = "/employees/{id}/events")
    public ResponseEntity getEvents(@PathVariable("id") String id) {
        ArrayList resources = new ArrayList<EmployeeAggregate>();
        eventStore.readEvents(id).asStream().forEach(resources::add);

        CollectionModel<EmployeeAggregate> model = CollectionModel.of(
            resources
        );

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    EntityModel<EmployeeAggregate> hateoas(EmployeeAggregate resource) {
        EntityModel<EmployeeAggregate> model = EntityModel.of(resource);

        model.add(Link.of("/employees/" + resource.getId()).withSelfRel());

        model.add(
            Link
                .of("/employees/" + resource.getId() + "/events")
                .withRel("events")
        );

        return model;
    }
}
