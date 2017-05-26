package org.coner.core.gateway;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.dao.RegistrationDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.RegistrationMapper;

import com.google.common.base.Preconditions;

public class RegistrationGateway extends MapStructAbstractGateway<
        RegistrationAddPayload,
        Registration,
        RegistrationHibernateEntity,
        RegistrationDao> {

    private final RegistrationMapper registrationMapper;
    private final EventMapper eventMapper;

    @Inject
    public RegistrationGateway(RegistrationMapper registrationMapper, EventMapper eventMapper, RegistrationDao dao) {
        super(
                registrationMapper::toHibernateEntity,
                registrationMapper::updateHibernateEntity,
                registrationMapper::toDomainEntity,
                registrationMapper::toDomainEntityList,
                dao
        );
        this.registrationMapper = registrationMapper;
        this.eventMapper = eventMapper;
    }

    public List<Registration> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        EventHibernateEntity hibernateEvent = eventMapper.toHibernateEntity(event);
        List<RegistrationHibernateEntity> registrations = dao.getAllWith(hibernateEvent);
        return registrationMapper.toDomainEntityList(registrations);
    }

}
