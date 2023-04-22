package moornmov.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import java.util.Date;
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
public class EmployeeAggregate {

    @AggregateIdentifier
    private String id;

    private String userName;
    private String userId;
    private String department;
    private String position;
    private Date enteringDate;
    private Boolean isManager;

    public EmployeeAggregate() {}

    @CommandHandler
    public EmployeeAggregate(RegistrationCommand command) {
        EmployeeAddedEvent event = new EmployeeAddedEvent();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getId() == null) event.setId(createUUID());

        apply(event);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(EmployeeAddedEvent event) {
        BeanUtils.copyProperties(event, this);
        //TODO: business logic here

    }
}
