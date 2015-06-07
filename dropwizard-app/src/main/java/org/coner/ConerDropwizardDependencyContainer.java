package org.coner;

import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
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

    void setHibernate(HibernateBundle<ConerDropwizardConfiguration> hibernate) {
        this.hibernate = hibernate;
    }

    EventApiDomainBoundary getEventApiDomainBoundary() {
        if (eventApiDomainBoundary == null) {
            eventApiDomainBoundary = new EventApiDomainBoundary();
        }
        return eventApiDomainBoundary;
    }

    void setEventApiDomainBoundary(EventApiDomainBoundary eventApiDomainBoundary) {
        this.eventApiDomainBoundary = eventApiDomainBoundary;
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

    void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    EventGateway getEventGateway() {
        if (eventGateway == null) {
            eventGateway = new EventGateway(getEventHibernateDomainBoundary(), getEventDao());
        }
        return eventGateway;
    }

    void setEventGateway(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    RegistrationApiDomainBoundary getRegistrationApiDomainBoundary() {
        if (registrationApiDomainBoundary == null) {
            registrationApiDomainBoundary = new RegistrationApiDomainBoundary();
        }
        return registrationApiDomainBoundary;
    }

    void setRegistrationApiDomainBoundary(RegistrationApiDomainBoundary registrationApiDomainBoundary) {
        this.registrationApiDomainBoundary = registrationApiDomainBoundary;
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

    void setRegistrationDao(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
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

    void setRegistrationGateway(RegistrationGateway registrationGateway) {
        this.registrationGateway = registrationGateway;
    }

    HandicapGroupApiDomainBoundary getHandicapGroupApiDomainBoundary() {
        if (handicapGroupApiDomainBoundary == null) {
            handicapGroupApiDomainBoundary = new HandicapGroupApiDomainBoundary();
        }
        return handicapGroupApiDomainBoundary;
    }

    void setHandicapGroupApiDomainBoundary(HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary) {
        this.handicapGroupApiDomainBoundary = handicapGroupApiDomainBoundary;
    }

    HandicapGroupHibernateDomainBoundary getHandicapGroupHibernateDomainBoundary() {
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

    HandicapGroupDao getHandicapGroupDao() {
        if (handicapGroupDao == null) {
            handicapGroupDao = new HandicapGroupDao(getHibernate().getSessionFactory());
        }
        return handicapGroupDao;
    }

    void setHandicapGroupDao(HandicapGroupDao handicapGroupDao) {
        this.handicapGroupDao = handicapGroupDao;
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

    void setHandicapGroupGateway(HandicapGroupGateway handicapGroupGateway) {
        this.handicapGroupGateway = handicapGroupGateway;
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

    void setHandicapGroupSetGateway(HandicapGroupSetGateway handicapGroupSetGateway) {
        this.handicapGroupSetGateway = handicapGroupSetGateway;
    }

    HandicapGroupSetApiDomainBoundary getHandicapGroupSetApiDomainBoundary() {
        if (handicapGroupSetApiDomainBoundary == null) {
            handicapGroupSetApiDomainBoundary = new HandicapGroupSetApiDomainBoundary();
        }
        return handicapGroupSetApiDomainBoundary;
    }

    void setHandicapGroupSetApiDomainBoundary(HandicapGroupSetApiDomainBoundary handicapGroupSetApiDomainBoundary) {
        this.handicapGroupSetApiDomainBoundary = handicapGroupSetApiDomainBoundary;
    }

    HandicapGroupSetHibernateDomainBoundary getHandicapGroupSetHibernateDomainBoundary() {
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

    HandicapGroupSetDao getHandicapGroupSetDao() {
        if (handicapGroupSetDao == null) {
            handicapGroupSetDao = new HandicapGroupSetDao(getHibernate().getSessionFactory());
        }
        return handicapGroupSetDao;
    }

    void setHandicapGroupSetDao(HandicapGroupSetDao handicapGroupSetDao) {
        this.handicapGroupSetDao = handicapGroupSetDao;
    }

    CompetitionGroupApiDomainBoundary getCompetitionGroupApiDomainBoundary() {
        if (competitionGroupApiDomainBoundary == null) {
            competitionGroupApiDomainBoundary = new CompetitionGroupApiDomainBoundary();
        }
        return competitionGroupApiDomainBoundary;
    }

    void setCompetitionGroupApiDomainBoundary(CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary) {
        this.competitionGroupApiDomainBoundary = competitionGroupApiDomainBoundary;
    }

    CompetitionGroupHibernateDomainBoundary getCompetitionGroupHibernateDomainBoundary() {
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

    CompetitionGroupDao getCompetitionGroupDao() {
        if (competitionGroupDao == null) {
            competitionGroupDao = new CompetitionGroupDao(getHibernate().getSessionFactory());
        }
        return competitionGroupDao;
    }

    void setCompetitionGroupDao(CompetitionGroupDao competitionGroupDao) {
        this.competitionGroupDao = competitionGroupDao;
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

    void setCompetitionGroupGateway(CompetitionGroupGateway competitionGroupGateway) {
        this.competitionGroupGateway = competitionGroupGateway;
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

    void setCompetitionGroupSetGateway(CompetitionGroupSetGateway competitionGroupSetGateway) {
        this.competitionGroupSetGateway = competitionGroupSetGateway;
    }

    CompetitionGroupSetApiDomainBoundary getCompetitionGroupSetApiDomainBoundary() {
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

    CompetitionGroupSetHibernateDomainBoundary getCompetitionGroupSetHibernateDomainBoundary() {
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

    CompetitionGroupSetDao getCompetitionGroupSetDao() {
        if (competitionGroupSetDao == null) {
            competitionGroupSetDao = new CompetitionGroupSetDao(getHibernate().getSessionFactory());
        }
        return competitionGroupSetDao;
    }

    void setCompetitionGroupSetDao(CompetitionGroupSetDao competitionGroupSetDao) {
        this.competitionGroupSetDao = competitionGroupSetDao;
    }


    ConerCoreService getConerCoreService() {
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
