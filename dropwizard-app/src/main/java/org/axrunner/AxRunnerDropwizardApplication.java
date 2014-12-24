package org.axrunner;

import com.fasterxml.jackson.databind.SerializationFeature;
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

    private final HibernateBundle<AxRunnerDropwizardConfiguration> hibernate = new HibernateBundle<AxRunnerDropwizardConfiguration>(
            Event.class
    ) {
        @Override
        public DataSourceFactory getDataSourceFactory(AxRunnerDropwizardConfiguration axRunnerDropwizardConfiguration) {
            return axRunnerDropwizardConfiguration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new AxRunnerDropwizardApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<AxRunnerDropwizardConfiguration> bootstrap
    ) {
        bootstrap.addBundle(hibernate);
        bootstrap.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public void run(
            AxRunnerDropwizardConfiguration axRunnerDropwizardConfiguration,
            Environment environment
    ) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        // init service
        EventBoundary eventBoundary = new EventBoundary();
        EventDao eventDao = new EventDao(hibernate.getSessionFactory());
        EventGateway eventGateway = new EventGateway(hibernate, eventBoundary, eventDao);
        AxRunnerCoreService axRunnerCoreService = new AxRunnerCoreService(eventGateway);

        // init resources
        EventsResource eventsResource = new EventsResource(eventBoundary, axRunnerCoreService);
        EventResource eventResource = new EventResource(eventBoundary, axRunnerCoreService);

        jersey.register(eventsResource);
        jersey.register(eventResource);

        // init exception mappers
        ResourceExceptionMapper resourceExceptionMapper = new ResourceExceptionMapper();

        jersey.register(resourceExceptionMapper);
    }
}
