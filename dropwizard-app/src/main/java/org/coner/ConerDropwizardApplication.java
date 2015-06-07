package org.coner;

import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.resource.*;
import org.coner.util.JacksonUtil;

import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.*;
import io.federecio.dropwizard.swagger.*;

public class ConerDropwizardApplication extends Application<ConerDropwizardConfiguration> {

    private ConerDropwizardDependencyContainer dependencies;

    /**
     * The main method of the application.
     *
     * @param args raw String arguments
     * @throws Exception any uncaught exception
     */
    public static void main(String[] args) throws Exception {
        new ConerDropwizardApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<ConerDropwizardConfiguration> bootstrap
    ) {
        bootstrap.addBundle(dependencies.getHibernate());
        bootstrap.addBundle(new SwaggerBundle<ConerDropwizardConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                    ConerDropwizardConfiguration configuration
            ) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });

        JacksonUtil.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
            ConerDropwizardConfiguration conerDropwizardConfiguration,
            Environment environment
    ) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        // init resources
        EventsResource eventsResource = new EventsResource(
                dependencies.getEventApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        EventResource eventResource = new EventResource(
                dependencies.getEventApiDomainBoundary(),
                dependencies.getConerCoreService());
        EventRegistrationsResource eventRegistrationsResource = new EventRegistrationsResource(
                dependencies.getRegistrationApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        EventRegistrationResource eventRegistrationResource = new EventRegistrationResource(
                dependencies.getRegistrationApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        HandicapGroupsResource handicapGroupsResource = new HandicapGroupsResource(
                dependencies.getHandicapGroupApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        HandicapGroupResource handicapGroupResource = new HandicapGroupResource(
                dependencies.getHandicapGroupApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        HandicapGroupSetsResource handicapGroupSetsResource = new HandicapGroupSetsResource(
                dependencies.getHandicapGroupSetApiDomainBoundary(),
                dependencies.getConerCoreService()
        );

        CompetitionGroupsResource competitionGroupsResource = new CompetitionGroupsResource(
                dependencies.getCompetitionGroupApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        CompetitionGroupResource competitionGroupResource = new CompetitionGroupResource(
                dependencies.getCompetitionGroupApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        CompetitionGroupSetsResource competitionGroupSetsResource = new CompetitionGroupSetsResource(
                dependencies.getCompetitionGroupSetApiDomainBoundary(),
                dependencies.getConerCoreService()
        );

        jersey.register(eventsResource);
        jersey.register(eventResource);
        jersey.register(eventRegistrationsResource);
        jersey.register(eventRegistrationResource);
        jersey.register(handicapGroupsResource);
        jersey.register(handicapGroupResource);
        jersey.register(handicapGroupSetsResource);
        jersey.register(competitionGroupsResource);
        jersey.register(competitionGroupResource);
        jersey.register(competitionGroupSetsResource);


        // init exception mappers
        WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

        jersey.register(webApplicationExceptionMapper);

    }
}
