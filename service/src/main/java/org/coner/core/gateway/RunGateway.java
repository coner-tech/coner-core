package org.coner.core.gateway;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.hibernate.dao.RunDao;
import org.coner.core.hibernate.entity.RunHibernateEntity;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.RegistrationMapper;
import org.coner.core.mapper.RunMapper;

import com.google.common.base.Preconditions;

public class RunGateway extends MapStructAbstractGateway<
        RunAddPayload,
        Run,
        RunHibernateEntity,
        RunDao> {

    private EventMapper eventMapper;
    private RegistrationMapper registrationMapper;

    @Inject
    public RunGateway(
            RunMapper runMapper,
            RunDao dao,
            EventMapper eventMapper,
            RegistrationMapper registrationMapper) {
        super(
                runMapper::toHibernateEntity,
                runMapper::updateHibernateEntity,
                runMapper::toDomainEntity,
                runMapper::toDomainEntityList,
                dao
        );
        this.eventMapper = eventMapper;
        this.registrationMapper = registrationMapper;
    }

    public Run findFirstInSequenceWithoutTime(Event event) {
        return hibernateEntityToDomainEntityConverter.convert(
                dao.findFirstInSequenceWithoutRawTime(eventMapper.toHibernateEntity(event))
        );
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

    public List<Run> getAllWith(Registration registration) {
        Preconditions.checkNotNull(registration);
        return hibernateEntitiesToDomainEntitiesConverter.convert(
                dao.getAllWith(registrationMapper.toHibernateEntity(registration))
        );
    }
}
