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
    private EventApiAddPayloadBoundary eventApiAddPayloadBoundary;
    private EventHibernateDomainBoundary eventHibernateDomainBoundary;
    private EventHibernateAddPayloadBoundary eventHibernateAddPayloadBoundary;
    private EventDao eventDao;
    private EventGateway eventGateway;
    private RegistrationService registrationService;
    private RegistrationApiDomainBoundary registrationApiDomainBoundary;
    private RegistrationApiAddPayloadBoundary registrationApiAddPayloadBoundary;
    private RegistrationHibernateDomainBoundary registrationHibernateDomainBoundary;
    private RegistrationHibernateAddPayloadBoundary registrationHibernateAddPayloadBoundary;
    private RegistrationDao registrationDao;
    private RegistrationGateway registrationGateway;
    private CompetitionGroupService competitionGroupService;
    private CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary;
    private CompetitionGroupApiAddPayloadBoundary competitionGroupApiAddPayloadBoundary;
    private CompetitionGroupHibernateDomainBoundary competitionGroupHibernateDomainBoundary;
    private CompetitionGroupHibernateAddPayloadBoundary competitionGroupHibernateAddPayloadBoundary;
    private CompetitionGroupDao competitionGroupDao;
    private CompetitionGroupGateway competitionGroupGateway;
    private CompetitionGroupSetService competitionGroupSetService;
    private CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary;
    private CompetitionGroupSetApiAddPayloadBoundary competitionGroupSetApiAddPayloadBoundary;
    private CompetitionGroupSetHibernateDomainBoundary competitionGroupSetHibernateDomainBoundary;
    private CompetitionGroupSetHibernateAddPayloadBoundary competitionGroupSetHibernateAddPayloadBoundary;
    private CompetitionGroupSetGateway competitionGroupSetGateway;
    private CompetitionGroupSetDao competitionGroupSetDao;
    private HandicapGroupService handicapGroupService;
    private HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary;
    private HandicapGroupApiAddPayloadBoundary handicapGroupApiAddPayloadBoundary;
    private HandicapGroupHibernateDomainBoundary handicapGroupHibernateDomainBoundary;
    private HandicapGroupHibernateAddPayloadBoundary handicapGroupHibernateAddPayloadBoundary;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupGateway handicapGroupGateway;
    private HandicapGroupSetService handicapGroupSetService;
    private HandicapGroupSetApiDomainBoundary handicapGroupSetApiDomainBoundary;
    private HandicapGroupSetApiAddPayloadBoundary handicapGroupSetApiAddPayloadBoundary;
    private HandicapGroupSetHibernateDomainBoundary handicapGroupSetHibernateDomainBoundary;
    private HandicapGroupSetHibernateAddPayloadBoundary handicapGroupSetHibernateAddPayloadBoundary;
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

    EventApiAddPayloadBoundary getEventApiAddPayloadBoundary() {
        if (eventApiAddPayloadBoundary == null) {
            eventApiAddPayloadBoundary = new EventApiAddPayloadBoundary();
        }
        return eventApiAddPayloadBoundary;
    }

    EventHibernateDomainBoundary getEventHibernateDomainBoundary() {
        if (eventHibernateDomainBoundary == null) {
            eventHibernateDomainBoundary = new EventHibernateDomainBoundary();
        }
        return eventHibernateDomainBoundary;
    }

    EventHibernateAddPayloadBoundary getEventHibernateAddPayloadBoundary() {
        if (eventHibernateAddPayloadBoundary == null) {
            eventHibernateAddPayloadBoundary = new EventHibernateAddPayloadBoundary();
        }
        return eventHibernateAddPayloadBoundary;
    }

    EventDao getEventDao() {
        if (eventDao == null) {
            eventDao = new EventDao(getHibernate().getSessionFactory());
        }
        return eventDao;
    }

    EventGateway getEventGateway() {
        if (eventGateway == null) {
            eventGateway = new EventGateway(
                    getEventHibernateDomainBoundary(),
                    getEventHibernateAddPayloadBoundary(),
                    getEventDao()
            );
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

    RegistrationApiAddPayloadBoundary getRegistrationApiAddPayloadBoundary() {
        if (registrationApiAddPayloadBoundary == null) {
            registrationApiAddPayloadBoundary = new RegistrationApiAddPayloadBoundary();
        }
        return registrationApiAddPayloadBoundary;
    }

    RegistrationHibernateDomainBoundary getRegistrationHibernateDomainBoundary() {
        if (registrationHibernateDomainBoundary == null) {
            registrationHibernateDomainBoundary = new RegistrationHibernateDomainBoundary(
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationHibernateDomainBoundary;
    }

    RegistrationHibernateAddPayloadBoundary getRegistrationHibernateAddPayloadBoundary() {
        if (registrationHibernateAddPayloadBoundary == null) {
            registrationHibernateAddPayloadBoundary = new RegistrationHibernateAddPayloadBoundary(
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationHibernateAddPayloadBoundary;
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
                    getRegistrationHibernateAddPayloadBoundary(),
                    getRegistrationDao(),
                    getEventHibernateDomainBoundary()
            );
        }
        return registrationGateway;
    }


    HandicapGroupService getHandicapGroupService() {
        if (handicapGroupService == null) {
            handicapGroupService = new HandicapGroupService(getHandicapGroupGateway());
        }
        return handicapGroupService;
    }

    HandicapGroupApiDomainBoundary getHandicapGroupApiDomainBoundary() {
        if (handicapGroupApiDomainBoundary == null) {
            handicapGroupApiDomainBoundary = new HandicapGroupApiDomainBoundary();
        }
        return handicapGroupApiDomainBoundary;
    }

    HandicapGroupApiAddPayloadBoundary getHandicapGroupApiAddPayloadBoundary() {
        if (handicapGroupApiAddPayloadBoundary == null) {
            handicapGroupApiAddPayloadBoundary = new HandicapGroupApiAddPayloadBoundary();
        }
        return handicapGroupApiAddPayloadBoundary;
    }

    HandicapGroupHibernateDomainBoundary getHandicapGroupHibernateDomainBoundary() {
        if (handicapGroupHibernateDomainBoundary == null) {
            handicapGroupHibernateDomainBoundary = new HandicapGroupHibernateDomainBoundary();
        }
        return handicapGroupHibernateDomainBoundary;
    }

    HandicapGroupHibernateAddPayloadBoundary getHandicapGroupHibernateAddPayloadBoundary() {
        if (handicapGroupHibernateAddPayloadBoundary == null) {
            handicapGroupHibernateAddPayloadBoundary = new HandicapGroupHibernateAddPayloadBoundary();
        }
        return handicapGroupHibernateAddPayloadBoundary;
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
                    getHandicapGroupHibernateAddPayloadBoundary(),
                    getHandicapGroupDao()
            );
        }
        return handicapGroupGateway;
    }

    HandicapGroupSetService getHandicapGroupSetService() {
        if (handicapGroupSetService == null) {
            handicapGroupSetService = new HandicapGroupSetService(getHandicapGroupSetGateway());
        }
        return handicapGroupSetService;
    }

    HandicapGroupSetGateway getHandicapGroupSetGateway() {
        if (handicapGroupSetGateway == null) {
            handicapGroupSetGateway = new HandicapGroupSetGateway(
                    getHandicapGroupSetHibernateDomainBoundary(),
                    getHandicapGroupSetHibernateAddPayloadBoundary(),
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

    HandicapGroupSetApiAddPayloadBoundary getHandicapGroupSetApiAddPayloadBoundary() {
        if (handicapGroupSetApiAddPayloadBoundary == null) {
            handicapGroupSetApiAddPayloadBoundary = new HandicapGroupSetApiAddPayloadBoundary();
        }
        return handicapGroupSetApiAddPayloadBoundary;
    }

    HandicapGroupSetHibernateDomainBoundary getHandicapGroupSetHibernateDomainBoundary() {
        if (handicapGroupSetHibernateDomainBoundary == null) {
            handicapGroupSetHibernateDomainBoundary = new HandicapGroupSetHibernateDomainBoundary();
        }
        return handicapGroupSetHibernateDomainBoundary;
    }

    HandicapGroupSetHibernateAddPayloadBoundary getHandicapGroupSetHibernateAddPayloadBoundary() {
        if (handicapGroupSetHibernateAddPayloadBoundary == null) {
            handicapGroupSetHibernateAddPayloadBoundary = new HandicapGroupSetHibernateAddPayloadBoundary();
        }
        return handicapGroupSetHibernateAddPayloadBoundary;
    }

    HandicapGroupSetDao getHandicapGroupSetDao() {
        if (handicapGroupSetDao == null) {
            handicapGroupSetDao = new HandicapGroupSetDao(getHibernate().getSessionFactory());
        }
        return handicapGroupSetDao;
    }

    CompetitionGroupService getCompetitionGroupService() {
        if (competitionGroupService == null) {
            competitionGroupService = new CompetitionGroupService(getCompetitionGroupGateway());
        }
        return competitionGroupService;
    }

    CompetitionGroupApiDomainBoundary getCompetitionGroupApiDomainBoundary() {
        if (competitionGroupApiDomainBoundary == null) {
            competitionGroupApiDomainBoundary = new CompetitionGroupApiDomainBoundary();
        }
        return competitionGroupApiDomainBoundary;
    }

    CompetitionGroupApiAddPayloadBoundary getCompetitionGroupApiAddPayloadBoundary() {
        if (competitionGroupApiAddPayloadBoundary == null) {
            competitionGroupApiAddPayloadBoundary = new CompetitionGroupApiAddPayloadBoundary();
        }
        return competitionGroupApiAddPayloadBoundary;
    }

    CompetitionGroupHibernateDomainBoundary getCompetitionGroupHibernateDomainBoundary() {
        if (competitionGroupHibernateDomainBoundary == null) {
            competitionGroupHibernateDomainBoundary = new CompetitionGroupHibernateDomainBoundary();
        }
        return competitionGroupHibernateDomainBoundary;
    }

    CompetitionGroupHibernateAddPayloadBoundary getCompetitionGroupHibernateAddPayloadBoundary() {
        if (competitionGroupHibernateAddPayloadBoundary == null) {
            competitionGroupHibernateAddPayloadBoundary = new CompetitionGroupHibernateAddPayloadBoundary();
        }
        return competitionGroupHibernateAddPayloadBoundary;
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
                    getCompetitionGroupHibernateAddPayloadBoundary(),
                    getCompetitionGroupDao());
        }
        return competitionGroupGateway;
    }

    CompetitionGroupSetService getCompetitionGroupSetService() {
        if (competitionGroupSetService == null) {
            competitionGroupSetService = new CompetitionGroupSetService(getCompetitionGroupSetGateway());
        }
        return competitionGroupSetService;
    }

    CompetitionGroupSetGateway getCompetitionGroupSetGateway() {
        if (competitionGroupSetGateway == null) {
            competitionGroupSetGateway = new CompetitionGroupSetGateway(
                    getCompetitionGroupSetHibernateDomainBoundary(),
                    getCompetitionGroupSetHibernateAddPayloadBoundary(),
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

    CompetitionGroupSetApiAddPayloadBoundary getCompetitionGroupSetApiAddPayloadBoundary() {
        if (competitionGroupSetApiAddPayloadBoundary == null) {
            competitionGroupSetApiAddPayloadBoundary = new CompetitionGroupSetApiAddPayloadBoundary();
        }
        return competitionGroupSetApiAddPayloadBoundary;
    }

    CompetitionGroupSetHibernateDomainBoundary getCompetitionGroupSetHibernateDomainBoundary() {
        if (competitionGroupSetHibernateDomainBoundary == null) {
            competitionGroupSetHibernateDomainBoundary = new CompetitionGroupSetHibernateDomainBoundary();
        }
        return competitionGroupSetHibernateDomainBoundary;
    }

    CompetitionGroupSetHibernateAddPayloadBoundary getCompetitionGroupSetHibernateAddPayloadBoundary() {
        if (competitionGroupSetHibernateAddPayloadBoundary == null) {
            competitionGroupSetHibernateAddPayloadBoundary = new CompetitionGroupSetHibernateAddPayloadBoundary();
        }
        return competitionGroupSetHibernateAddPayloadBoundary;
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
                    getCompetitionGroupService(),
                    getCompetitionGroupSetService(),
                    getHandicapGroupService(),
                    getHandicapGroupSetService()
            );
        }
        return conerCoreService;
    }
}
