package org.axrunner.hibernate.gateway;

import com.google.common.base.Preconditions;
import io.dropwizard.hibernate.HibernateBundle;
import org.axrunner.AxRunnerDropwizardConfiguration;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.boundary.RegistrationBoundary;
import org.axrunner.core.domain.Event;
import org.axrunner.core.domain.Registration;
import org.axrunner.hibernate.dao.RegistrationDao;

import java.util.List;

/**
 *
 */
public class RegistrationGateway extends HibernateGateway {

    private final RegistrationBoundary registrationBoundary;
    private final EventBoundary eventBoundary;
    private final RegistrationDao registrationDao;

    public RegistrationGateway(HibernateBundle<AxRunnerDropwizardConfiguration> hibernate, RegistrationBoundary registrationBoundary, EventBoundary eventBoundary, RegistrationDao registrationDao) {
        super(hibernate);
        this.registrationBoundary = registrationBoundary;
        this.eventBoundary = eventBoundary;
        this.registrationDao = registrationDao;
    }

    public List<Registration> getAllWith(Event event) {
        Preconditions.checkNotNull(event);
        org.axrunner.hibernate.entity.Event hibernateEvent = eventBoundary.toHibernateEntity(event);
        List<org.axrunner.hibernate.entity.Registration> registrations = registrationDao.getAllWith(hibernateEvent);
        return registrationBoundary.toDomainEntities(registrations);
    }
}
