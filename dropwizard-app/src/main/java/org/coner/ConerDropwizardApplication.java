package org.coner;

import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.gateway.*;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.*;
import org.coner.hibernate.entity.*;
import org.coner.resource.*;
import org.coner.util.JacksonUtil;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.*;
import io.federecio.dropwizard.swagger.*;

public class ConerDropwizardApplication extends Application<ConerDropwizardConfiguration> {

    private HibernateBundle<ConerDropwizardConfiguration> hibernate;
    private EventBoundary eventBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private RegistrationBoundary registrationBoundary;
    private RegistrationDao registrationDao;
    private RegistrationGateway registrationGateway;
    private CompetitionGroupBoundary competitionGroupBoundary;
    private CompetitionGroupDao competitionGroupDao;
    private CompetitionGroupGateway competitionGroupGateway;
    private CompetitionGroupSetBoundary competitionGroupSetBoundary;
    private CompetitionGroupSetGateway competitionGroupSetGateway;
    private CompetitionGroupSetDao competitionGroupSetDao;
    private HandicapGroupBoundary handicapGroupBoundary;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupGateway handicapGroupGateway;
    private HandicapGroupSetBoundary handicapGroupSetBoundary;
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
        EventsResource eventsResource = new EventsResource(getEventBoundary(), getConerCoreService());
        EventResource eventResource = new EventResource(getEventBoundary(), getConerCoreService());
        EventRegistrationsResource eventRegistrationsResource = new EventRegistrationsResource(
                getEventBoundary(),
                getRegistrationBoundary(),
                getConerCoreService()
        );
        EventRegistrationResource eventRegistrationResource = new EventRegistrationResource(
                getEventBoundary(),
                getRegistrationBoundary(),
                getConerCoreService()
        );
        HandicapGroupsResource handicapGroupsResource = new HandicapGroupsResource(
                getHandicapGroupBoundary(),
                getConerCoreService()
        );
        HandicapGroupResource handicapGroupResource = new HandicapGroupResource(
                getHandicapGroupBoundary(),
                getConerCoreService()
        );
        HandicapGroupSetsResource handicapGroupSetsResource = new HandicapGroupSetsResource(
                getHandicapGroupSetBoundary(),
                getConerCoreService()
        );

        CompetitionGroupsResource competitionGroupsResource = new CompetitionGroupsResource(
                getCompetitionGroupBoundary(),
                getConerCoreService()
        );
        CompetitionGroupResource competitionGroupResource = new CompetitionGroupResource(
                getCompetitionGroupBoundary(),
                getConerCoreService()
        );
        CompetitionGroupSetsResource competitionGroupSetsResource = new CompetitionGroupSetsResource(
                getCompetitionGroupSetBoundary(),
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
            hibernate = new HibernateBundle<ConerDropwizardConfiguration>(
                    Event.class,
                    RegistrationHibernateEntity.class,
                    HandicapGroupHibernateEntity.class,
                    HandicapGroupSetHibernateEntity.class,
                    CompetitionGroupHibernateEntity.class,
                    CompetitionGroupSetHibernateEntity.class
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
            eventGateway = new EventGateway(getEventBoundary(), getEventDao());
        }
        return eventGateway;
    }

    void setEventGateway(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    private RegistrationBoundary getRegistrationBoundary() {
        if (registrationBoundary == null) {
            registrationBoundary = new RegistrationBoundary(getEventBoundary());
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
            registrationGateway = new RegistrationGateway(
                    getRegistrationBoundary(),
                    getEventBoundary(),
                    getRegistrationDao()
            );
        }
        return registrationGateway;
    }

    void setRegistrationGateway(RegistrationGateway registrationGateway) {
        this.registrationGateway = registrationGateway;
    }

    private HandicapGroupBoundary getHandicapGroupBoundary() {
        if (handicapGroupBoundary == null) {
            handicapGroupBoundary = new HandicapGroupBoundary();
        }
        return handicapGroupBoundary;
    }

    void setHandicapGroupBoundary(HandicapGroupBoundary handicapGroupBoundary) {
        this.handicapGroupBoundary = handicapGroupBoundary;
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
            handicapGroupGateway = new HandicapGroupGateway(getHandicapGroupBoundary(), getHandicapGroupDao());
        }
        return handicapGroupGateway;
    }

    void setHandicapGroupGateway(HandicapGroupGateway handicapGroupGateway) {
        this.handicapGroupGateway = handicapGroupGateway;
    }

    private HandicapGroupSetGateway getHandicapGroupSetGateway() {
        if (handicapGroupSetGateway == null) {
            handicapGroupSetGateway = new HandicapGroupSetGateway(
                    getHandicapGroupSetBoundary(),
                    getHandicapGroupSetDao()
            );
        }
        return handicapGroupSetGateway;
    }

    void setHandicapGroupSetGateway(HandicapGroupSetGateway handicapGroupSetGateway) {
        this.handicapGroupSetGateway = handicapGroupSetGateway;
    }

    private HandicapGroupSetBoundary getHandicapGroupSetBoundary() {
        if (handicapGroupSetBoundary == null) {
            handicapGroupSetBoundary = new HandicapGroupSetBoundary();
        }
        return handicapGroupSetBoundary;
    }

    void setHandicapGroupSetBoundary(HandicapGroupSetBoundary handicapGroupSetBoundary) {
        this.handicapGroupSetBoundary = handicapGroupSetBoundary;
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

    private CompetitionGroupBoundary getCompetitionGroupBoundary() {
        if (competitionGroupBoundary == null) {
            competitionGroupBoundary = new CompetitionGroupBoundary();
        }
        return competitionGroupBoundary;
    }

    void setCompetitionGroupBoundary(CompetitionGroupBoundary competitionGroupBoundary) {
        this.competitionGroupBoundary = competitionGroupBoundary;
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
                    getCompetitionGroupBoundary(),
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
                    getCompetitionGroupSetBoundary(),
                    getCompetitionGroupSetDao()
            );
        }
        return competitionGroupSetGateway;
    }

    void setCompetitionGroupSetGateway(CompetitionGroupSetGateway competitionGroupSetGateway) {
        this.competitionGroupSetGateway = competitionGroupSetGateway;
    }

    private CompetitionGroupSetBoundary getCompetitionGroupSetBoundary() {
        if (competitionGroupSetBoundary == null) {
            competitionGroupSetBoundary = new CompetitionGroupSetBoundary();
        }
        return competitionGroupSetBoundary;
    }

    void setCompetitionGroupSetBoundary(CompetitionGroupSetBoundary competitionGroupSetBoundary) {
        this.competitionGroupSetBoundary = competitionGroupSetBoundary;
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
