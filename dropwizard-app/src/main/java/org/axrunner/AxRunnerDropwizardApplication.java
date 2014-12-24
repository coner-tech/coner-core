package org.axrunner;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Preconditions;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.exception.ResourceExceptionMapper;
import org.axrunner.hibernate.dao.EventDao;
import org.axrunner.hibernate.entity.Event;
import org.axrunner.hibernate.gateway.EventGateway;
import org.axrunner.resource.EventResource;
import org.axrunner.resource.EventsResource;

/**
 *
 */
public class AxRunnerDropwizardApplication extends Application<AxRunnerDropwizardConfiguration> {

    private HibernateBundle<AxRunnerDropwizardConfiguration> hibernate;
    private EventBoundary eventBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private AxRunnerCoreService axRunnerCoreService;

    public static void main(String[] args) throws Exception {
        new AxRunnerDropwizardApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<AxRunnerDropwizardConfiguration> bootstrap
    ) {
        bootstrap.addBundle(getHibernate());
        bootstrap.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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

        jersey.register(eventsResource);
        jersey.register(eventResource);

        // init exception mappers
        ResourceExceptionMapper resourceExceptionMapper = new ResourceExceptionMapper();

        jersey.register(resourceExceptionMapper);
    }

    private HibernateBundle<AxRunnerDropwizardConfiguration> getHibernate() {
        if (hibernate == null) {
            hibernate = new HibernateBundle<AxRunnerDropwizardConfiguration>(
                    Event.class
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
            eventBoundary = new EventBoundary();
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

    private AxRunnerCoreService getAxRunnerCoreService() {
        Preconditions.checkNotNull(eventGateway);
        if (axRunnerCoreService == null) {
            axRunnerCoreService = new AxRunnerCoreService(eventGateway);
        }
        return axRunnerCoreService;
    }

    void setAxRunnerCoreService(AxRunnerCoreService axRunnerCoreService) {
        this.axRunnerCoreService = axRunnerCoreService;
    }
}
