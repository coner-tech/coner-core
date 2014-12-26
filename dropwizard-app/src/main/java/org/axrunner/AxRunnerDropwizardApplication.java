package org.axrunner;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.boundary.RegistrationBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.exception.WebApplicationExceptionMapper;
import org.axrunner.hibernate.dao.EventDao;
import org.axrunner.hibernate.dao.RegistrationDao;
import org.axrunner.hibernate.entity.Event;
import org.axrunner.hibernate.entity.Registration;
import org.axrunner.hibernate.gateway.EventGateway;
import org.axrunner.hibernate.gateway.RegistrationGateway;
import org.axrunner.resource.EventRegistrationsResource;
import org.axrunner.resource.EventResource;
import org.axrunner.resource.EventsResource;
import org.axrunner.util.JacksonUtil;

/**
 *
 */
public class AxRunnerDropwizardApplication extends Application<AxRunnerDropwizardConfiguration> {

    private HibernateBundle<AxRunnerDropwizardConfiguration> hibernate;
    private EventBoundary eventBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private RegistrationBoundary registrationBoundary;
    private RegistrationDao registrationDao;
    private RegistrationGateway registrationGateway;
    private AxRunnerCoreService axRunnerCoreService;

    public static void main(String[] args) throws Exception {
        new AxRunnerDropwizardApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<AxRunnerDropwizardConfiguration> bootstrap
    ) {
        bootstrap.addBundle(getHibernate());

        JacksonUtil.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
            AxRunnerDropwizardConfiguration axRunnerDropwizardConfiguration,
            Environment environment
    ) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        // init resources
        EventsResource eventsResource = new EventsResource(getEventBoundary(), getAxRunnerCoreService());
        EventResource eventResource = new EventResource(getEventBoundary(), getAxRunnerCoreService());
        EventRegistrationsResource eventRegistrationsResource = new EventRegistrationsResource(getEventBoundary(), getRegistrationBoundary(), getAxRunnerCoreService());

        jersey.register(eventsResource);
        jersey.register(eventResource);
        jersey.register(eventRegistrationsResource);

        // init exception mappers
        WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

        jersey.register(webApplicationExceptionMapper);
    }

    private HibernateBundle<AxRunnerDropwizardConfiguration> getHibernate() {
        if (hibernate == null) {
            hibernate = new HibernateBundle<AxRunnerDropwizardConfiguration>(
                    Event.class,
                    Registration.class
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(AxRunnerDropwizardConfiguration axRunnerDropwizardConfiguration) {
                    return axRunnerDropwizardConfiguration.getDataSourceFactory();
                }
            };
        }
        return hibernate;
    }

    void setHibernate(HibernateBundle<AxRunnerDropwizardConfiguration> hibernate) {
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
            this.eventGateway = new EventGateway(getHibernate(), getEventBoundary(), getEventDao());
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
            registrationGateway = new RegistrationGateway(getHibernate(), getRegistrationBoundary(), getEventBoundary(), getRegistrationDao());
        }
        return registrationGateway;
    }

    void setRegistrationGateway(RegistrationGateway registrationGateway) {
        this.registrationGateway = registrationGateway;
    }

    private AxRunnerCoreService getAxRunnerCoreService() {
        if (axRunnerCoreService == null) {
            axRunnerCoreService = new AxRunnerCoreService(getEventGateway(), getRegistrationGateway());
        }
        return axRunnerCoreService;
    }

    void setAxRunnerCoreService(AxRunnerCoreService axRunnerCoreService) {
        this.axRunnerCoreService = axRunnerCoreService;
    }
}
