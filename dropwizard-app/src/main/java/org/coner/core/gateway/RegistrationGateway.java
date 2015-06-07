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
        RegistrationBoundary,
        RegistrationDao> {

    private final EventBoundary eventBoundary;

    public RegistrationGateway(RegistrationBoundary boundary, RegistrationDao dao, EventBoundary eventBoundary) {
        super(boundary, dao);
        this.eventBoundary = eventBoundary;
    }

    public List<Registration> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        EventHibernateEntity hibernateEvent = eventBoundary.toHibernateEntity(event);
        List<RegistrationHibernateEntity> registrations = getDao().getAllWith(hibernateEvent);
        return getBoundary().toDomainEntities(registrations);
    }

}
