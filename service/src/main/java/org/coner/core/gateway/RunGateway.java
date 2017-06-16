package org.coner.core.gateway;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.hibernate.dao.RunDao;
import org.coner.core.hibernate.entity.RunHibernateEntity;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.RunMapper;

import com.google.common.base.Preconditions;

public class RunGateway extends MapStructAbstractGateway<
        RunAddPayload,
        Run,
        RunHibernateEntity,
        RunDao> {

    private EventMapper eventMapper;

    @Inject
    public RunGateway(
            RunMapper runMapper,
            RunDao dao,
            EventMapper eventMapper
    ) {
        super(
                runMapper::toHibernateEntity,
                runMapper::updateHibernateEntity,
                runMapper::toDomainEntity,
                runMapper::toDomainEntityList,
                dao
        );
        this.eventMapper = eventMapper;
    }

    public Run findLastInSequenceForEvent(Event event) {
        return hibernateEntityToDomainEntityConverter.convert(
                dao.findLastInSequenceFor(eventMapper.toHibernateEntity(event))
        );
    }

    @Override
    public List<Run> getAll() {
        throw new UnsupportedOperationException();
    }

    public List<Run> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        return hibernateEntitiesToDomainEntitiesConverter.convert(
                dao.getAllWith(eventMapper.toHibernateEntity(event))
        );
    }
}
