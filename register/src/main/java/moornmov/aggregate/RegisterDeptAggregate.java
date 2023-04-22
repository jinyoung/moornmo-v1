package moornmov.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import moornmov.command.*;
import moornmov.event.*;
import moornmov.query.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Data
@ToString
public class RegisterDeptAggregate {

    @AggregateIdentifier
    private String id;

    private String name;

    public RegisterDeptAggregate() {}

    @CommandHandler
    public RegisterDeptAggregate(RegistrationCommand command) {
        DepartmentAddedEvent event = new DepartmentAddedEvent();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getId() == null) event.setId(createUUID());

        apply(event);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(DepartmentAddedEvent event) {
        BeanUtils.copyProperties(event, this);
        //TODO: business logic here

    }
}
