package org.coner;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.dao.RegistrationDao;
import org.coner.hibernate.entity.Event;
import org.coner.hibernate.entity.Registration;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.RegistrationGateway;
import org.coner.resource.EventRegistrationsResource;
import org.coner.resource.EventResource;
import org.coner.resource.EventsResource;
import org.coner.util.JacksonUtil;

/**
 *
 */
public class ConerDropwizardApplication extends Application<ConerDropwizardConfiguration> {

    private HibernateBundle<ConerDropwizardConfiguration> hibernate;
    private EventBoundary eventBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private RegistrationBoundary registrationBoundary;
    private RegistrationDao registrationDao;
    private RegistrationGateway registrationGateway;
    private ConerCoreService conerCoreService;

    public static void main(String[] args) throws Exception {
        new ConerDropwizardApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<ConerDropwizardConfiguration> bootstrap
    ) {
        bootstrap.addBundle(getHibernate());

        JacksonUtil.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
            ConerDropwizardConfiguration conerDropwizardConfiguration,
            Environment environment
    ) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        // init resources
        EventsResource eventsResource = new EventsResource(getEventBoundary(), getConerCoreService());
        EventResource eventResource = new EventResource(getEventBoundary(), getConerCoreService());
        EventRegistrationsResource eventRegistrationsResource = new EventRegistrationsResource(getEventBoundary(), getRegistrationBoundary(), getConerCoreService());

        jersey.register(eventsResource);
        jersey.register(eventResource);
        jersey.register(eventRegistrationsResource);

        // init exception mappers
        WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

        jersey.register(webApplicationExceptionMapper);
    }

    private HibernateBundle<ConerDropwizardConfiguration> getHibernate() {
        if (hibernate == null) {
            hibernate = new HibernateBundle<ConerDropwizardConfiguration>(
                    Event.class,
                    Registration.class
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(ConerDropwizardConfiguration conerDropwizardConfiguration) {
                    return conerDropwizardConfiguration.getDataSourceFactory();
                }
            };
        }
        return hibernate;
    }

    void setHibernate(HibernateBundle<ConerDropwizardConfiguration> hibernate) {
        this.hibernate = hibernate;
    }

    private EventBoundary getEventBoundary() {
        if (eventBoundary == null) {
            eventBoundary = EventBoundary.getInstance();
        }
        return eventBoundary;
    }

    void setEventBoundary(EventBoundary eventBoundary) {
        this.eventBoundary = eventBoundary;
    }

    private EventDao getEventDao() {
        if (eventDao == null) {
            eventDao = new EventDao(getHibernate().getSessionFactory());
        }
        return eventDao;
    }

    void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    private EventGateway getEventGateway() {
        if (eventGateway == null) {
            this.eventGateway = new EventGateway(getEventBoundary(), getEventDao());
        }
        return eventGateway;
    }

    void setEventGateway(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    private RegistrationBoundary getRegistrationBoundary() {
        if (registrationBoundary == null) {
            registrationBoundary = RegistrationBoundary.getInstance();
        }
        return registrationBoundary;
    }

    void setRegistrationBoundary(RegistrationBoundary registrationBoundary) {
        this.registrationBoundary = registrationBoundary;
    }

    private RegistrationDao getRegistrationDao() {
        if (registrationDao == null) {
            registrationDao = new RegistrationDao(getHibernate().getSessionFactory());
        }
        return registrationDao;
    }

    void setRegistrationDao(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    private RegistrationGateway getRegistrationGateway() {
        if (registrationGateway == null) {
            registrationGateway = new RegistrationGateway(getRegistrationBoundary(), getEventBoundary(), getRegistrationDao());
        }
        return registrationGateway;
    }

    void setRegistrationGateway(RegistrationGateway registrationGateway) {
        this.registrationGateway = registrationGateway;
    }

    private ConerCoreService getConerCoreService() {
        if (conerCoreService == null) {
            conerCoreService = new ConerCoreService(getEventGateway(), getRegistrationGateway());
        }
        return conerCoreService;
    }

    void setConerCoreService(ConerCoreService conerCoreService) {
        this.conerCoreService = conerCoreService;
    }
}
