package org.coner;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.*;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.gateway.CompetitionGroupGateway;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.HandicapGroupGateway;
import org.coner.core.gateway.RegistrationGateway;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.CompetitionGroupDao;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.dao.HandicapGroupDao;
import org.coner.hibernate.dao.RegistrationDao;
import org.coner.hibernate.entity.CompetitionGroup;
import org.coner.hibernate.entity.Event;
import org.coner.hibernate.entity.HandicapGroup;
import org.coner.hibernate.entity.Registration;
import org.coner.resource.CompetitionGroupsResource;
import org.coner.resource.EventRegistrationResource;
import org.coner.resource.EventRegistrationsResource;
import org.coner.resource.EventResource;
import org.coner.resource.EventsResource;
import org.coner.resource.HandicapGroupsResource;
import org.coner.util.JacksonUtil;

/**
 * The Dropwizard application which exposes the Coner core REST interface.
 */
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
    private HandicapGroupBoundary handicapGroupBoundary;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupGateway handicapGroupGateway;
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
        bootstrap.addBundle(new SwaggerBundle<ConerDropwizardConfiguration>());

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
        HandicapGroupsResource handicapGroupsResource = new HandicapGroupsResource(getHandicapGroupBoundary(),
                getConerCoreService());

        CompetitionGroupsResource competitionGroupsResource = new CompetitionGroupsResource(
                getCompetitionGroupBoundary(),
                getConerCoreService()
        );

        jersey.register(eventsResource);
        jersey.register(eventResource);
        jersey.register(eventRegistrationsResource);
        jersey.register(eventRegistrationResource);
        jersey.register(handicapGroupsResource);
        jersey.register(competitionGroupsResource);

        // init exception mappers
        WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

        jersey.register(webApplicationExceptionMapper);
    }

    /**
     * Lazy initializer for the HibernateBundle.
     *
     * @return the HibernateBundle
     */
    private HibernateBundle<ConerDropwizardConfiguration> getHibernate() {
        if (hibernate == null) {
            hibernate = new HibernateBundle<ConerDropwizardConfiguration>(
                    Event.class,
                    Registration.class,
                    HandicapGroup.class,
                    CompetitionGroup.class
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

    /**
     * Lazy initializer for the EventBoundary.
     *
     * @return the EventBoundary
     */
    private EventBoundary getEventBoundary() {
        if (eventBoundary == null) {
            eventBoundary = new EventBoundary();
        }
        return eventBoundary;
    }

    void setEventBoundary(EventBoundary eventBoundary) {
        this.eventBoundary = eventBoundary;
    }

    /**
     * Lazy initializer for the EventDao.
     *
     * @return the EventDao
     */
    private EventDao getEventDao() {
        if (eventDao == null) {
            eventDao = new EventDao(getHibernate().getSessionFactory());
        }
        return eventDao;
    }

    void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    /**
     * Lazy initializer for the EventGateway.
     *
     * @return the EventGateway
     */
    private EventGateway getEventGateway() {
        if (eventGateway == null) {
            eventGateway = new EventGateway(getEventBoundary(), getEventDao());
        }
        return eventGateway;
    }

    void setEventGateway(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    /**
     * Lazy initializer for RegistrationBoundary.
     *
     * @return the RegistrationBoundary
     */
    private RegistrationBoundary getRegistrationBoundary() {
        if (registrationBoundary == null) {
            registrationBoundary = new RegistrationBoundary(getEventBoundary());
        }
        return registrationBoundary;
    }

    void setRegistrationBoundary(RegistrationBoundary registrationBoundary) {
        this.registrationBoundary = registrationBoundary;
    }

    /**
     * Lazy initializer for the RegistrationDao.
     *
     * @return the RegistrationDao
     */
    private RegistrationDao getRegistrationDao() {
        if (registrationDao == null) {
            registrationDao = new RegistrationDao(getHibernate().getSessionFactory());
        }
        return registrationDao;
    }

    void setRegistrationDao(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    /**
     * Lazy initializer for the RegistrationGateway.
     *
     * @return the RegistrationGateway
     */
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

    /**
     * Lazy initializer for the HandicapGroupBoundary.
     *
     * @return the HandicapGroupBoundary
     */
    private HandicapGroupBoundary getHandicapGroupBoundary() {
        if (handicapGroupBoundary == null) {
            handicapGroupBoundary = new HandicapGroupBoundary();
        }
        return handicapGroupBoundary;
    }

    void setHandicapGroupBoundary(HandicapGroupBoundary handicapGroupBoundary) {
        this.handicapGroupBoundary = handicapGroupBoundary;
    }

    /**
     * Lazy initializer for the HandicapGroupDao.
     *
     * @return the HandicapGroupDao
     */
    private HandicapGroupDao getHandicapGroupDao() {
        if (handicapGroupDao == null) {
            handicapGroupDao = new HandicapGroupDao(getHibernate().getSessionFactory());
        }
        return handicapGroupDao;
    }

    void setHandicapGroupDao(HandicapGroupDao handicapGroupDao) {
        this.handicapGroupDao = handicapGroupDao;
    }

    /**
     * Lazy initializer for the HandicapGroupGateway.
     *
     * @return the EventGateway
     */
    private HandicapGroupGateway getHandicapGroupGateway() {
        if (handicapGroupGateway == null) {
            handicapGroupGateway = new HandicapGroupGateway(getHandicapGroupBoundary(), getHandicapGroupDao());
        }
        return handicapGroupGateway;
    }

    void setHandicapGroupGateway(HandicapGroupGateway handicapGroupGateway) {
        this.handicapGroupGateway = handicapGroupGateway;
    }

    /**
     * Lazy initializer for the CompetitionGroupBoundary.
     *
     * @return the CompetitionGroupBoundary
     */
    private CompetitionGroupBoundary getCompetitionGroupBoundary() {
        if (competitionGroupBoundary == null) {
            competitionGroupBoundary = new CompetitionGroupBoundary();
        }
        return competitionGroupBoundary;
    }

    void setCompetitionGroupBoundary(CompetitionGroupBoundary competitionGroupBoundary) {
        this.competitionGroupBoundary = competitionGroupBoundary;
    }

    /**
     * Lazy initializer for the CompetitionGroupDao.
     *
     * @return the CompetitionGroupDao
     */
    private CompetitionGroupDao getCompetitionGroupDao() {
        if (competitionGroupDao == null) {
            competitionGroupDao = new CompetitionGroupDao(getHibernate().getSessionFactory());
        }
        return competitionGroupDao;
    }

    void setCompetitionGroupDao(CompetitionGroupDao competitionGroupDao) {
        this.competitionGroupDao = competitionGroupDao;
    }

    /**
     * Lazy initializer for the CompetitionGroupGateway.
     *
     * @return the CompetitionGroupGateway
     */
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


    /**
     * Lazy initializer for the ConerCoreService.
     *
     * @return the ConerCoreService
     */
    private ConerCoreService getConerCoreService() {
        if (conerCoreService == null) {
            conerCoreService = new ConerCoreService(
                    getEventGateway(),
                    getRegistrationGateway(),
                    getHandicapGroupGateway(),
                    getCompetitionGroupGateway()
            );
        }
        return conerCoreService;
    }

    void setConerCoreService(ConerCoreService conerCoreService) {
        this.conerCoreService = conerCoreService;
    }
}
