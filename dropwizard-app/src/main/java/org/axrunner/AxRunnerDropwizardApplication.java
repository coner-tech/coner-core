package org.axrunner;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.hibernate.entity.Event;
import org.axrunner.hibernate.gateway.EventGateway;
import org.axrunner.resource.EventsResource;

import java.text.DateFormat;

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
        bootstrap.getObjectMapper().setDateFormat(DateFormat.getDateTimeInstance());
    }

    @Override
    public void run(
            AxRunnerDropwizardConfiguration axRunnerDropwizardConfiguration,
            Environment environment
    ) throws Exception {

        // init service
        EventBoundary eventBoundary = new EventBoundary();
        EventGateway eventGateway = new EventGateway(hibernate, eventBoundary);
        AxRunnerCoreService axRunnerCoreService = new AxRunnerCoreService(eventGateway);

        // init resources
        EventsResource eventsResource = new EventsResource(eventBoundary, axRunnerCoreService);

        environment.jersey().register(eventsResource);
    }
}
