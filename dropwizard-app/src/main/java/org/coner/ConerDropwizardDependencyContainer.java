package org.coner;

import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.service.*;
import org.coner.core.gateway.*;
import org.coner.hibernate.dao.*;
import org.coner.hibernate.entity.HibernateEntity;

import com.google.common.collect.ImmutableList;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.*;
import java.util.Set;
import org.reflections.Reflections;

public class ConerDropwizardDependencyContainer {

    private HibernateBundle<ConerDropwizardConfiguration> hibernate;
    private EventService eventService;
    private EventApiDomainBoundary eventApiDomainBoundary;
    private EventHibernateDomainBoundary eventHibernateDomainBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private RegistrationService registrationService;
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

    HibernateBundle<ConerDropwizardConfiguration> getHibernate() {
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

    EventService getEventService() {
        if (eventService == null) {
            eventService = new EventService(getEventGateway());
        }
        return eventService;
    }

    EventApiDomainBoundary getEventApiDomainBoundary() {
        if (eventApiDomainBoundary == null) {
            eventApiDomainBoundary = new EventApiDomainBoundary();
        }
        return eventApiDomainBoundary;
    }

    EventHibernateDomainBoundary getEventHibernateDomainBoundary() {
        if (eventHibernateDomainBoundary == null) {
            eventHibernateDomainBoundary = new EventHibernateDomainBoundary();
        }
        return eventHibernateDomainBoundary;
    }

    EventDao getEventDao() {
        if (eventDao == null) {
            eventDao = new EventDao(getHibernate().getSessionFactory());
        }
        return eventDao;
    }

    EventGateway getEventGateway() {
        if (eventGateway == null) {
            eventGateway = new EventGateway(getEventHibernateDomainBoundary(), getEventDao());
        }
        return eventGateway;
    }

    RegistrationService getRegistrationService() {
        if (registrationService == null) {
            registrationService = new RegistrationService(getRegistrationGateway());
        }
        return registrationService;
    }

    RegistrationApiDomainBoundary getRegistrationApiDomainBoundary() {
        if (registrationApiDomainBoundary == null) {
            registrationApiDomainBoundary = new RegistrationApiDomainBoundary();
        }
        return registrationApiDomainBoundary;
    }

    RegistrationHibernateDomainBoundary getRegistrationHibernateDomainBoundary() {
        if (registrationHibernateDomainBoundary == null) {
            registrationHibernateDomainBoundary = new RegistrationHibernateDomainBoundary(
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationHibernateDomainBoundary;
    }

    RegistrationDao getRegistrationDao() {
        if (registrationDao == null) {
            registrationDao = new RegistrationDao(getHibernate().getSessionFactory());
        }
        return registrationDao;
    }

    RegistrationGateway getRegistrationGateway() {
        if (registrationGateway == null) {
            registrationGateway = new RegistrationGateway(
                    getRegistrationHibernateDomainBoundary(),
                    getRegistrationDao(),
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationGateway;
    }

    HandicapGroupApiDomainBoundary getHandicapGroupApiDomainBoundary() {
        if (handicapGroupApiDomainBoundary == null) {
            handicapGroupApiDomainBoundary = new HandicapGroupApiDomainBoundary();
        }
        return handicapGroupApiDomainBoundary;
    }

    HandicapGroupHibernateDomainBoundary getHandicapGroupHibernateDomainBoundary() {
        if (handicapGroupHibernateDomainBoundary == null) {
            handicapGroupHibernateDomainBoundary = new HandicapGroupHibernateDomainBoundary();
        }
        return handicapGroupHibernateDomainBoundary;
    }

    HandicapGroupDao getHandicapGroupDao() {
        if (handicapGroupDao == null) {
            handicapGroupDao = new HandicapGroupDao(getHibernate().getSessionFactory());
        }
        return handicapGroupDao;
    }

    HandicapGroupGateway getHandicapGroupGateway() {
        if (handicapGroupGateway == null) {
            handicapGroupGateway = new HandicapGroupGateway(
                    getHandicapGroupHibernateDomainBoundary(),
                    getHandicapGroupDao()
            );
        }
        return handicapGroupGateway;
    }

    HandicapGroupSetGateway getHandicapGroupSetGateway() {
        if (handicapGroupSetGateway == null) {
            handicapGroupSetGateway = new HandicapGroupSetGateway(
                    getHandicapGroupSetHibernateDomainBoundary(),
                    getHandicapGroupSetDao()
            );
        }
        return handicapGroupSetGateway;
    }

    HandicapGroupSetApiDomainBoundary getHandicapGroupSetApiDomainBoundary() {
        if (handicapGroupSetApiDomainBoundary == null) {
            handicapGroupSetApiDomainBoundary = new HandicapGroupSetApiDomainBoundary();
        }
        return handicapGroupSetApiDomainBoundary;
    }

    HandicapGroupSetHibernateDomainBoundary getHandicapGroupSetHibernateDomainBoundary() {
        if (handicapGroupSetHibernateDomainBoundary == null) {
            handicapGroupSetHibernateDomainBoundary = new HandicapGroupSetHibernateDomainBoundary();
        }
        return handicapGroupSetHibernateDomainBoundary;
    }

    HandicapGroupSetDao getHandicapGroupSetDao() {
        if (handicapGroupSetDao == null) {
            handicapGroupSetDao = new HandicapGroupSetDao(getHibernate().getSessionFactory());
        }
        return handicapGroupSetDao;
    }

    CompetitionGroupApiDomainBoundary getCompetitionGroupApiDomainBoundary() {
        if (competitionGroupApiDomainBoundary == null) {
            competitionGroupApiDomainBoundary = new CompetitionGroupApiDomainBoundary();
        }
        return competitionGroupApiDomainBoundary;
    }

    CompetitionGroupHibernateDomainBoundary getCompetitionGroupHibernateDomainBoundary() {
        if (competitionGroupHibernateDomainBoundary == null) {
            competitionGroupHibernateDomainBoundary = new CompetitionGroupHibernateDomainBoundary();
        }
        return competitionGroupHibernateDomainBoundary;
    }

    CompetitionGroupDao getCompetitionGroupDao() {
        if (competitionGroupDao == null) {
            competitionGroupDao = new CompetitionGroupDao(getHibernate().getSessionFactory());
        }
        return competitionGroupDao;
    }

    CompetitionGroupGateway getCompetitionGroupGateway() {
        if (competitionGroupGateway == null) {
            this.competitionGroupGateway = new CompetitionGroupGateway(
                    getCompetitionGroupHibernateDomainBoundary(),
                    getCompetitionGroupDao()
            );
        }
        return competitionGroupGateway;
    }

    CompetitionGroupSetGateway getCompetitionGroupSetGateway() {
        if (competitionGroupSetGateway == null) {
            competitionGroupSetGateway = new CompetitionGroupSetGateway(
                    getCompetitionGroupSetHibernateDomainBoundary(),
                    getCompetitionGroupSetDao()
            );
        }
        return competitionGroupSetGateway;
    }

    CompetitionGroupSetApiDomainBoundary getCompetitionGroupSetApiDomainBoundary() {
        if (competitionGroupSetApiDomainBoundary == null) {
            competitionGroupSetApiDomainBoundary = new CompetitionGroupSetApiDomainBoundary();
        }
        return competitionGroupSetApiDomainBoundary;
    }

    CompetitionGroupSetHibernateDomainBoundary getCompetitionGroupSetHibernateDomainBoundary() {
        if (competitionGroupSetHibernateDomainBoundary == null) {
            competitionGroupSetHibernateDomainBoundary = new CompetitionGroupSetHibernateDomainBoundary();
        }
        return competitionGroupSetHibernateDomainBoundary;
    }

    CompetitionGroupSetDao getCompetitionGroupSetDao() {
        if (competitionGroupSetDao == null) {
            competitionGroupSetDao = new CompetitionGroupSetDao(getHibernate().getSessionFactory());
        }
        return competitionGroupSetDao;
    }

    ConerCoreService getConerCoreService() {
        if (conerCoreService == null) {
            conerCoreService = new ConerCoreService(
                    getEventService(),
                    getRegistrationService(),
                    getCompetitionGroupGateway(),
                    getCompetitionGroupSetGateway(),
                    getHandicapGroupGateway(),
                    getHandicapGroupSetGateway()
            );
        }
        return conerCoreService;
    }
}
