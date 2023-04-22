package moornmov.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import moornmov.aggregate.*;
import moornmov.event.*;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("employeeSearch")
public class EmployeeSearchCQRSHandlerReusingAggregate {

    @Autowired
    private EmployeeReadModelRepository repository;

    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    @QueryHandler
    public List<EmployeeReadModel> handle(EmployeeSearchQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<EmployeeReadModel> handle(EmployeeSearchSingleQuery query) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenEmployeeAdded_then_CREATE(EmployeeAddedEvent event)
        throws Exception {
        EmployeeReadModel entity = new EmployeeReadModel();
        EmployeeAggregate aggregate = new EmployeeAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);

        queryUpdateEmitter.emit(
            EmployeeSearchQuery.class,
            query -> true,
            entity
        );
    }
}
