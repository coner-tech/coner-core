package org.coner.core.gateway;

import org.coner.boundary.*;
import org.coner.core.domain.*;
import org.coner.hibernate.dao.RegistrationDao;

import com.google.common.base.*;
import java.util.List;

public class RegistrationGateway {

    private final RegistrationBoundary registrationBoundary;
    private final EventBoundary eventBoundary;
    private final RegistrationDao registrationDao;

    public RegistrationGateway(
            RegistrationBoundary registrationBoundary,
            EventBoundary eventBoundary,
            RegistrationDao registrationDao) {
        this.registrationBoundary = registrationBoundary;
        this.eventBoundary = eventBoundary;
        this.registrationDao = registrationDao;
    }

    public Registration findById(String registrationId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(registrationId));
        org.coner.hibernate.entity.Registration hibernateRegistration = registrationDao.findById(registrationId);
        return registrationBoundary.toDomainEntity(hibernateRegistration);
    }

    public List<Registration> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        org.coner.hibernate.entity.Event hibernateEvent = eventBoundary.toHibernateEntity(event);
        List<org.coner.hibernate.entity.Registration> registrations = registrationDao.getAllWith(hibernateEvent);
        return registrationBoundary.toDomainEntities(registrations);
    }

    public void create(Registration registration) {
        Preconditions.checkNotNull(registration);
        org.coner.hibernate.entity.Registration hibernateRegistration =
                registrationBoundary.toHibernateEntity(registration);
        registrationDao.create(hibernateRegistration);
        registrationBoundary.merge(hibernateRegistration, registration);
    }

}
