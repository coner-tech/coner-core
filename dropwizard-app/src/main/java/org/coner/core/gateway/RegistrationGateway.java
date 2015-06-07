package org.coner.core.gateway;

import org.coner.boundary.*;
import org.coner.core.domain.*;
import org.coner.hibernate.dao.RegistrationDao;
import org.coner.hibernate.entity.*;

import com.google.common.base.Preconditions;
import java.util.List;

public class RegistrationGateway extends AbstractGateway<
        Registration,
        RegistrationHibernateEntity,
        RegistrationHibernateDomainBoundary,
        RegistrationDao> {

    private final EventHibernateDomainBoundary eventBoundary;

    public RegistrationGateway(
            RegistrationHibernateDomainBoundary boundary,
            RegistrationDao dao,
            EventHibernateDomainBoundary eventBoundary) {
        super(boundary, dao);
        this.eventBoundary = eventBoundary;
    }

    public List<Registration> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        EventHibernateEntity hibernateEvent = eventBoundary.toLocalEntity(event);
        List<RegistrationHibernateEntity> registrations = getDao().getAllWith(hibernateEvent);
        return getBoundary().toRemoteEntities(registrations);
    }

}
