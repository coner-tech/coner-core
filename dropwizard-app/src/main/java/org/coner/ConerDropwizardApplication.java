package org.coner;

import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.resource.CompetitionGroupResource;
import org.coner.resource.CompetitionGroupSetResource;
import org.coner.resource.CompetitionGroupSetsResource;
import org.coner.resource.CompetitionGroupsResource;
import org.coner.resource.EventRegistrationResource;
import org.coner.resource.EventRegistrationsResource;
import org.coner.resource.EventResource;
import org.coner.resource.EventsResource;
import org.coner.resource.HandicapGroupResource;
import org.coner.resource.HandicapGroupSetsResource;
import org.coner.resource.HandicapGroupsResource;
import org.coner.util.JacksonUtil;

import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

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
        ConerDropwizardDependencyContainer dependencies = getDependencies();

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
        ConerDropwizardDependencyContainer dependencies = getDependencies();
        JerseyEnvironment jersey = environment.jersey();

        // init resources
        EventsResource eventsResource = new EventsResource(
                dependencies.getConerCoreService(),
                dependencies.getEventApiDomainBoundary(),
                dependencies.getEventApiAddPayloadBoundary()
        );
        EventResource eventResource = new EventResource(
                dependencies.getEventApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        EventRegistrationsResource eventRegistrationsResource = new EventRegistrationsResource(
                dependencies.getConerCoreService(),
                dependencies.getRegistrationApiDomainBoundary(),
                dependencies.getRegistrationApiAddPayloadBoundary()
        );
        EventRegistrationResource eventRegistrationResource = new EventRegistrationResource(
                dependencies.getRegistrationApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        HandicapGroupsResource handicapGroupsResource = new HandicapGroupsResource(
                dependencies.getConerCoreService(),
                dependencies.getHandicapGroupApiDomainBoundary(),
                dependencies.getHandicapGroupApiAddPayloadBoundary()
        );
        HandicapGroupResource handicapGroupResource = new HandicapGroupResource(
                dependencies.getHandicapGroupApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        HandicapGroupSetsResource handicapGroupSetsResource = new HandicapGroupSetsResource(
                dependencies.getConerCoreService(),
                dependencies.getHandicapGroupSetApiDomainBoundary(),
                dependencies.getHandicapGroupSetApiAddPayloadBoundary()
        );

        CompetitionGroupsResource competitionGroupsResource = new CompetitionGroupsResource(
                dependencies.getConerCoreService(),
                dependencies.getCompetitionGroupApiDomainBoundary(),
                dependencies.getCompetitionGroupApiAddPayloadBoundary()
        );
        CompetitionGroupResource competitionGroupResource = new CompetitionGroupResource(
                dependencies.getCompetitionGroupApiDomainBoundary(),
                dependencies.getConerCoreService()
        );
        CompetitionGroupSetsResource competitionGroupSetsResource = new CompetitionGroupSetsResource(
                dependencies.getConerCoreService(),
                dependencies.getCompetitionGroupSetApiDomainBoundary(),
                dependencies.getCompetitionGroupSetApiAddPayloadBoundary()
        );

        CompetitionGroupSetResource competitionGroupSetResource = new CompetitionGroupSetResource(
                dependencies.getConerCoreService(), dependencies.getCompetitionGroupSetApiDomainBoundary()
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
        jersey.register(competitionGroupSetResource);

        // init exception mappers
        WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

        jersey.register(webApplicationExceptionMapper);
    }

    private ConerDropwizardDependencyContainer getDependencies() {
        if (dependencies == null) {
            dependencies = new ConerDropwizardDependencyContainer();
        }
        return dependencies;
    }

    void setDependencies(ConerDropwizardDependencyContainer conerDropwizardDependencyContainer) {
        this.dependencies = conerDropwizardDependencyContainer;
    }
}
