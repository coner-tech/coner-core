package org.coner.core.gateway;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.boundary.EventHibernateDomainBoundary;
import org.coner.core.boundary.RegistrationHibernateAddPayloadBoundary;
import org.coner.core.boundary.RegistrationHibernateDomainBoundary;
import org.coner.core.api.entity.Event;
import org.coner.core.api.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.dao.RegistrationDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;

import com.google.common.base.Preconditions;

public class RegistrationGateway extends AbstractGateway<
        Registration,
        RegistrationHibernateEntity,
        RegistrationAddPayload,
        RegistrationHibernateDomainBoundary,
        RegistrationHibernateAddPayloadBoundary,
        RegistrationDao> {

    private final EventHibernateDomainBoundary eventEntityBoundary;

    @Inject
    public RegistrationGateway(
            RegistrationHibernateDomainBoundary entityBoundary,
            RegistrationHibernateAddPayloadBoundary addPayloadBoundary,
            RegistrationDao dao,
            EventHibernateDomainBoundary eventEntityBoundary) {
        super(entityBoundary, addPayloadBoundary, dao);
        this.eventEntityBoundary = eventEntityBoundary;
    }

    public List<Registration> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        EventHibernateEntity hibernateEvent = eventEntityBoundary.toLocalEntity(event);
        List<RegistrationHibernateEntity> registrations = dao.getAllWith(hibernateEvent);
        return entityBoundary.toRemoteEntities(registrations);
    }

}
