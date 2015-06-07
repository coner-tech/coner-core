package org.coner;

import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.gateway.*;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.*;
import org.coner.hibernate.entity.HibernateEntity;
import org.coner.resource.*;
import org.coner.util.JacksonUtil;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.*;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.*;
import io.federecio.dropwizard.swagger.*;
import java.util.Set;
import org.reflections.Reflections;

public class ConerDropwizardApplication extends Application<ConerDropwizardConfiguration> {

    private HibernateBundle<ConerDropwizardConfiguration> hibernate;
    private EventApiDomainBoundary eventApiDomainBoundary;
    private EventHibernateDomainBoundary eventHibernateDomainBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private RegistrationApiDomainBoundary registrationApiDomainBoundary;
    private RegistrationHibernateDomainBoundary registrationHibernateDomainBoundary;
    private RegistrationDao registrationDao;
    private RegistrationGateway registrationGateway;
    private CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary;
    private CompetitionGroupHibernateDomainBoundary competitionGroupHibernateDomainBoundary;
    private CompetitionGroupDao competitionGroupDao;
    private CompetitionGroupGateway competitionGroupGateway;
    private CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary;
    private CompetitionGroupSetHibernateDomainBoundary competitionGroupSetHibernateDomainBoundary;
    private CompetitionGroupSetGateway competitionGroupSetGateway;
    private CompetitionGroupSetDao competitionGroupSetDao;
    private HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary;
    private HandicapGroupHibernateDomainBoundary handicapGroupHibernateDomainBoundary;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupGateway handicapGroupGateway;
    private HandicapGroupSetApiDomainBoundary handicapGroupSetApiDomainBoundary;
    private HandicapGroupSetHibernateDomainBoundary handicapGroupSetHibernateDomainBoundary;
    private HandicapGroupSetDao handicapGroupSetDao;
    private HandicapGroupSetGateway handicapGroupSetGateway;
    private ConerCoreService conerCoreService;

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
        bootstrap.addBundle(getHibernate());
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
        EventsResource eventsResource = new EventsResource(getEventApiDomainBoundary(), getConerCoreService());
        EventResource eventResource = new EventResource(getEventApiDomainBoundary(), getConerCoreService());
        EventRegistrationsResource eventRegistrationsResource = new EventRegistrationsResource(
                getRegistrationApiDomainBoundary(),
                getConerCoreService()
        );
        EventRegistrationResource eventRegistrationResource = new EventRegistrationResource(
                getRegistrationApiDomainBoundary(),
                getConerCoreService()
        );
        HandicapGroupsResource handicapGroupsResource = new HandicapGroupsResource(
                getHandicapGroupApiDomainBoundary(),
                getConerCoreService()
        );
        HandicapGroupResource handicapGroupResource = new HandicapGroupResource(
                getHandicapGroupApiDomainBoundary(),
                getConerCoreService()
        );
        HandicapGroupSetsResource handicapGroupSetsResource = new HandicapGroupSetsResource(
                getHandicapGroupSetApiDomainBoundary(),
                getConerCoreService()
        );

        CompetitionGroupsResource competitionGroupsResource = new CompetitionGroupsResource(
                getCompetitionGroupApiDomainBoundary(),
                getConerCoreService()
        );
        CompetitionGroupResource competitionGroupResource = new CompetitionGroupResource(
                getCompetitionGroupApiDomainBoundary(),
                getConerCoreService()
        );
        CompetitionGroupSetsResource competitionGroupSetsResource = new CompetitionGroupSetsResource(
                getCompetitionGroupSetApiDomainBoundary(),
                getConerCoreService()
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

    private HibernateBundle<ConerDropwizardConfiguration> getHibernate() {
        if (hibernate == null) {
            Reflections r = new Reflections("org.coner.hibernate.entity");
            Set<Class<? extends HibernateEntity>> hibernateEntityClasses = r.getSubTypesOf(HibernateEntity.class);
            hibernate = new HibernateBundle<ConerDropwizardConfiguration>(
                    ImmutableList.copyOf(hibernateEntityClasses),
                    new SessionFactoryFactory()
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(
                        ConerDropwizardConfiguration conerDropwizardConfiguration) {
                    return conerDropwizardConfiguration.getDataSourceFactory();
                }
            };
        }
        return hibernate;
    }

    void setHibernate(HibernateBundle<ConerDropwizardConfiguration> hibernate) {
        this.hibernate = hibernate;
    }

    private EventApiDomainBoundary getEventApiDomainBoundary() {
        if (eventApiDomainBoundary == null) {
            eventApiDomainBoundary = new EventApiDomainBoundary();
        }
        return eventApiDomainBoundary;
    }

    void setEventApiDomainBoundary(EventApiDomainBoundary eventApiDomainBoundary) {
        this.eventApiDomainBoundary = eventApiDomainBoundary;
    }

    private EventHibernateDomainBoundary getEventHibernateDomainBoundary() {
        if (eventHibernateDomainBoundary == null) {
            eventHibernateDomainBoundary = new EventHibernateDomainBoundary();
        }
        return eventHibernateDomainBoundary;
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
            eventGateway = new EventGateway(getEventHibernateDomainBoundary(), getEventDao());
        }
        return eventGateway;
    }

    void setEventGateway(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    private RegistrationApiDomainBoundary getRegistrationApiDomainBoundary() {
        if (registrationApiDomainBoundary == null) {
            registrationApiDomainBoundary = new RegistrationApiDomainBoundary();
        }
        return registrationApiDomainBoundary;
    }

    void setRegistrationApiDomainBoundary(RegistrationApiDomainBoundary registrationApiDomainBoundary) {
        this.registrationApiDomainBoundary = registrationApiDomainBoundary;
    }

    private RegistrationHibernateDomainBoundary getRegistrationHibernateDomainBoundary() {
        if (registrationHibernateDomainBoundary == null) {
            registrationHibernateDomainBoundary = new RegistrationHibernateDomainBoundary(
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationHibernateDomainBoundary;
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
            registrationGateway = new RegistrationGateway(
                    getRegistrationHibernateDomainBoundary(),
                    getRegistrationDao(),
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationGateway;
    }

    void setRegistrationGateway(RegistrationGateway registrationGateway) {
        this.registrationGateway = registrationGateway;
    }

    private HandicapGroupApiDomainBoundary getHandicapGroupApiDomainBoundary() {
        if (handicapGroupApiDomainBoundary == null) {
            handicapGroupApiDomainBoundary = new HandicapGroupApiDomainBoundary();
        }
        return handicapGroupApiDomainBoundary;
    }

    void setHandicapGroupApiDomainBoundary(HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary) {
        this.handicapGroupApiDomainBoundary = handicapGroupApiDomainBoundary;
    }

    private HandicapGroupHibernateDomainBoundary getHandicapGroupHibernateDomainBoundary() {
        if (handicapGroupHibernateDomainBoundary == null) {
            handicapGroupHibernateDomainBoundary = new HandicapGroupHibernateDomainBoundary();
        }
        return handicapGroupHibernateDomainBoundary;
    }

    void setHandicapGroupHibernateDomainBoundary(
            HandicapGroupHibernateDomainBoundary handicapGroupHibernateDomainBoundary
    ) {
        this.handicapGroupHibernateDomainBoundary = handicapGroupHibernateDomainBoundary;
    }

    private HandicapGroupDao getHandicapGroupDao() {
        if (handicapGroupDao == null) {
            handicapGroupDao = new HandicapGroupDao(getHibernate().getSessionFactory());
        }
        return handicapGroupDao;
    }

    void setHandicapGroupDao(HandicapGroupDao handicapGroupDao) {
        this.handicapGroupDao = handicapGroupDao;
    }

    private HandicapGroupGateway getHandicapGroupGateway() {
        if (handicapGroupGateway == null) {
            handicapGroupGateway = new HandicapGroupGateway(
                    getHandicapGroupHibernateDomainBoundary(),
                    getHandicapGroupDao()
            );
        }
        return handicapGroupGateway;
    }

    void setHandicapGroupGateway(HandicapGroupGateway handicapGroupGateway) {
        this.handicapGroupGateway = handicapGroupGateway;
    }

    private HandicapGroupSetGateway getHandicapGroupSetGateway() {
        if (handicapGroupSetGateway == null) {
            handicapGroupSetGateway = new HandicapGroupSetGateway(
                    getHandicapGroupSetHibernateDomainBoundary(),
                    getHandicapGroupSetDao()
            );
        }
        return handicapGroupSetGateway;
    }

    void setHandicapGroupSetGateway(HandicapGroupSetGateway handicapGroupSetGateway) {
        this.handicapGroupSetGateway = handicapGroupSetGateway;
    }

    private HandicapGroupSetApiDomainBoundary getHandicapGroupSetApiDomainBoundary() {
        if (handicapGroupSetApiDomainBoundary == null) {
            handicapGroupSetApiDomainBoundary = new HandicapGroupSetApiDomainBoundary();
        }
        return handicapGroupSetApiDomainBoundary;
    }

    void setHandicapGroupSetApiDomainBoundary(HandicapGroupSetApiDomainBoundary handicapGroupSetApiDomainBoundary) {
        this.handicapGroupSetApiDomainBoundary = handicapGroupSetApiDomainBoundary;
    }

    private HandicapGroupSetHibernateDomainBoundary getHandicapGroupSetHibernateDomainBoundary() {
        if (handicapGroupSetHibernateDomainBoundary == null) {
            handicapGroupSetHibernateDomainBoundary = new HandicapGroupSetHibernateDomainBoundary();
        }
        return handicapGroupSetHibernateDomainBoundary;
    }

    void setHandicapGroupSetHibernateDomainBoundary(
            HandicapGroupSetHibernateDomainBoundary handicapGroupSetHibernateDomainBoundary
    ) {
        this.handicapGroupSetHibernateDomainBoundary = handicapGroupSetHibernateDomainBoundary;
    }

    private HandicapGroupSetDao getHandicapGroupSetDao() {
        if (handicapGroupSetDao == null) {
            handicapGroupSetDao = new HandicapGroupSetDao(getHibernate().getSessionFactory());
        }
        return handicapGroupSetDao;
    }

    void setHandicapGroupSetDao(HandicapGroupSetDao handicapGroupSetDao) {
        this.handicapGroupSetDao = handicapGroupSetDao;
    }

    private CompetitionGroupApiDomainBoundary getCompetitionGroupApiDomainBoundary() {
        if (competitionGroupApiDomainBoundary == null) {
            competitionGroupApiDomainBoundary = new CompetitionGroupApiDomainBoundary();
        }
        return competitionGroupApiDomainBoundary;
    }

    void setCompetitionGroupApiDomainBoundary(CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary) {
        this.competitionGroupApiDomainBoundary = competitionGroupApiDomainBoundary;
    }

    private CompetitionGroupHibernateDomainBoundary getCompetitionGroupHibernateDomainBoundary() {
        if (competitionGroupHibernateDomainBoundary == null) {
            competitionGroupHibernateDomainBoundary = new CompetitionGroupHibernateDomainBoundary();
        }
        return competitionGroupHibernateDomainBoundary;
    }

    void setCompetitionGroupHibernateDomainBoundary(
            CompetitionGroupHibernateDomainBoundary competitionGroupHibernateDomainBoundary
    ) {
        this.competitionGroupHibernateDomainBoundary = competitionGroupHibernateDomainBoundary;
    }

    private CompetitionGroupDao getCompetitionGroupDao() {
        if (competitionGroupDao == null) {
            competitionGroupDao = new CompetitionGroupDao(getHibernate().getSessionFactory());
        }
        return competitionGroupDao;
    }

    void setCompetitionGroupDao(CompetitionGroupDao competitionGroupDao) {
        this.competitionGroupDao = competitionGroupDao;
    }

    private CompetitionGroupGateway getCompetitionGroupGateway() {
        if (competitionGroupGateway == null) {
            this.competitionGroupGateway = new CompetitionGroupGateway(
                    getCompetitionGroupHibernateDomainBoundary(),
                    getCompetitionGroupDao()
            );
        }
        return competitionGroupGateway;
    }

    void setCompetitionGroupGateway(CompetitionGroupGateway competitionGroupGateway) {
        this.competitionGroupGateway = competitionGroupGateway;
    }

    private CompetitionGroupSetGateway getCompetitionGroupSetGateway() {
        if (competitionGroupSetGateway == null) {
            competitionGroupSetGateway = new CompetitionGroupSetGateway(
                    getCompetitionGroupSetHibernateDomainBoundary(),
                    getCompetitionGroupSetDao()
            );
        }
        return competitionGroupSetGateway;
    }

    void setCompetitionGroupSetGateway(CompetitionGroupSetGateway competitionGroupSetGateway) {
        this.competitionGroupSetGateway = competitionGroupSetGateway;
    }

    private CompetitionGroupSetApiDomainBoundary getCompetitionGroupSetApiDomainBoundary() {
        if (competitionGroupSetApiDomainBoundary == null) {
            competitionGroupSetApiDomainBoundary = new CompetitionGroupSetApiDomainBoundary();
        }
        return competitionGroupSetApiDomainBoundary;
    }

    void setCompetitionGroupSetApiDomainBoundary(
            CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary
    ) {
        this.competitionGroupSetApiDomainBoundary = competitionGroupSetApiDomainBoundary;
    }

    private CompetitionGroupSetHibernateDomainBoundary getCompetitionGroupSetHibernateDomainBoundary() {
        if (competitionGroupSetHibernateDomainBoundary == null) {
            competitionGroupSetHibernateDomainBoundary = new CompetitionGroupSetHibernateDomainBoundary();
        }
        return competitionGroupSetHibernateDomainBoundary;
    }

    void setCompetitionGroupSetHibernateDomainBoundary(
            CompetitionGroupSetHibernateDomainBoundary competitionGroupSetHibernateDomainBoundary
    ) {
        this.competitionGroupSetHibernateDomainBoundary = competitionGroupSetHibernateDomainBoundary;
    }

    private CompetitionGroupSetDao getCompetitionGroupSetDao() {
        if (competitionGroupSetDao == null) {
            competitionGroupSetDao = new CompetitionGroupSetDao(getHibernate().getSessionFactory());
        }
        return competitionGroupSetDao;
    }

    void setCompetitionGroupSetDao(CompetitionGroupSetDao competitionGroupSetDao) {
        this.competitionGroupSetDao = competitionGroupSetDao;
    }


    private ConerCoreService getConerCoreService() {
        if (conerCoreService == null) {
            conerCoreService = new ConerCoreService(
                    getEventGateway(),
                    getRegistrationGateway(),
                    getCompetitionGroupGateway(),
                    getCompetitionGroupSetGateway(),
                    getHandicapGroupGateway(),
                    getHandicapGroupSetGateway()
            );
        }
        return conerCoreService;
    }

    void setConerCoreService(ConerCoreService conerCoreService) {
        this.conerCoreService = conerCoreService;
    }
}
