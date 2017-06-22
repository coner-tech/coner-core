package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.testing.junit.DAOTestRule;

public class RegistrationDaoTest extends AbstractDaoTest {

    private RegistrationDao dao;
    private EventDao eventDao;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupSetDao handicapGroupSetDao;
    private CompetitionGroupDao competitionGroupDao;
    private CompetitionGroupSetDao competitionGroupSetDao;

    private Prerequisites prerequisites;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(RegistrationHibernateEntity.class)
            .addEntityClass(EventHibernateEntity.class)
            .addEntityClass(HandicapGroupHibernateEntity.class)
            .addEntityClass(HandicapGroupSetHibernateEntity.class)
            .addEntityClass(CompetitionGroupHibernateEntity.class)
            .addEntityClass(CompetitionGroupSetHibernateEntity.class)
            .build();

    @Before
    public void setup() {
        SessionFactory sessionFactory = daoTestRule.getSessionFactory();
        dao = new RegistrationDao(sessionFactory);
        eventDao = new EventDao(sessionFactory);
        handicapGroupDao = new HandicapGroupDao(sessionFactory);
        handicapGroupSetDao = new HandicapGroupSetDao(sessionFactory);
        competitionGroupDao = new CompetitionGroupDao(sessionFactory);
        competitionGroupSetDao = new CompetitionGroupSetDao(sessionFactory);

        prerequisites = setupPrerequisites();
    }

    @Test
    public void itShouldRoundTrip() {
        // create and store an entity
        RegistrationHibernateEntity inEntity = buildUnsavedRegistration();
        daoTestRule.inTransaction(() -> dao.create(inEntity));

        // clear the cache
        daoTestRule.getSessionFactory().getCurrentSession().clear();

        // read a fresh instance of the stored entity
        RegistrationHibernateEntity outEntity = daoTestRule.inTransaction(() -> dao.findById(inEntity.getId()));

        // assertions
        assertThat(outEntity)
                .isNotNull()
                .isNotSameAs(inEntity)
                .extracting(
                        it -> it.getId(),
                        it -> it.getEvent().getId(),
                        it -> it.getHandicapGroup().getId()
                )
                .containsExactly(
                        inEntity.getId(),
                        prerequisites.event.getId(),
                        prerequisites.handicapGroup.getId()
                );
    }

    @Test
    public void itShouldAddMultipleSimilarRegistrations() {
        List<RegistrationHibernateEntity> inEntities = Arrays.asList(
                buildUnsavedRegistration(),
                buildUnsavedRegistration(),
                buildUnsavedRegistration()
        );

        daoTestRule.inTransaction(() -> inEntities.forEach(it -> dao.create(it)));
    }

    @Test
    public void itShouldThrowWhenHandicapGroupIsNull() {
        RegistrationHibernateEntity entity = buildUnsavedRegistration();
        entity.setHandicapGroup(null);

        try {
            daoTestRule.inTransaction(() -> dao.create(entity));
            failBecauseExceptionWasNotThrown(PersistenceException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(PersistenceException.class)
                    .hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }

    private Prerequisites setupPrerequisites() {
        Prerequisites prerequisites = new Prerequisites();
        prerequisites.handicapGroup = HibernateEntityTestUtils.fullHandicapGroup();
        prerequisites.handicapGroup.setId(null);
        prerequisites.handicapGroupSet = HibernateEntityTestUtils.fullHandicapGroupSet();
        prerequisites.handicapGroupSet.setId(null);
        prerequisites.handicapGroupSet.setHandicapGroups(Sets.newHashSet(prerequisites.handicapGroup));
        prerequisites.competitionGroup = HibernateEntityTestUtils.fullCompetitionGroup();
        prerequisites.competitionGroup.setId(null);
        prerequisites.competitionGroupSet = HibernateEntityTestUtils.fullCompetitionGroupSet();
        prerequisites.competitionGroupSet.setId(null);
        prerequisites.competitionGroupSet.setCompetitionGroups(Sets.newHashSet(prerequisites.competitionGroup));
        prerequisites.event = HibernateEntityTestUtils.fullEvent();
        prerequisites.event.setHandicapGroupSet(prerequisites.handicapGroupSet);
        prerequisites.event.setCompetitionGroupSet(prerequisites.competitionGroupSet);
        prerequisites.event.setId(null);

        daoTestRule.inTransaction(() -> {
            handicapGroupDao.create(prerequisites.handicapGroup);
            handicapGroupSetDao.create(prerequisites.handicapGroupSet);
            competitionGroupDao.create(prerequisites.competitionGroup);
            competitionGroupSetDao.create(prerequisites.competitionGroupSet);
            eventDao.create(prerequisites.event);
        });

        return prerequisites;
    }

    private static class Prerequisites {
        private HandicapGroupHibernateEntity handicapGroup;
        private HandicapGroupSetHibernateEntity handicapGroupSet;
        private CompetitionGroupHibernateEntity competitionGroup;
        private CompetitionGroupSetHibernateEntity competitionGroupSet;
        private EventHibernateEntity event;
    }

    private RegistrationHibernateEntity buildUnsavedRegistration() {
        RegistrationHibernateEntity registration = HibernateEntityTestUtils.fullRegistration();
        registration.setId(null);
        registration.setEvent(prerequisites.event);
        registration.setHandicapGroup(prerequisites.handicapGroup);
        return registration;
    }
}
