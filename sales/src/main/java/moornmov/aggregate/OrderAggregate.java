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
public class OrderAggregate {

    @AggregateIdentifier
    private String id;

    private Date accountDate;
    private String type;
    private Integer number;
    private String client;
    private Date orderDate;
    private Date deliveryDate;
    private String manager;
    private String registerName;
    private Integer orderItem;
    private Integer orderCount;
    private Integer cost;
    private String memo;

    public OrderAggregate() {}

    @CommandHandler
    public OrderAggregate(OrderCommand command) {
        OrderAddedEvent event = new OrderAddedEvent();
        BeanUtils.copyProperties(command, event);

        //TODO: check key generation is properly done
        if (event.getId() == null) event.setId(createUUID());

        apply(event);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(OrderAddedEvent event) {
        BeanUtils.copyProperties(event, this);
        //TODO: business logic here

    }
}
