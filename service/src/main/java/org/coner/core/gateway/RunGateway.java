package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.hibernate.dao.RunDao;
import org.coner.core.hibernate.entity.RunHibernateEntity;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.RunMapper;

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
}
