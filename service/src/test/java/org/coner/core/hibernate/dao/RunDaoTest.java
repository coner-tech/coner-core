package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.persistence.PersistenceException;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.PersonHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.hibernate.entity.RunHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import io.dropwizard.testing.junit.DAOTestRule;

public class RunDaoTest extends AbstractDaoTest {

    private RunDao dao;
    private EventDao eventDao;
    private RegistrationDao registrationDao;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupSetDao handicapGroupSetDao;
    private CompetitionGroupDao competitionGroupDao;
    private CompetitionGroupSetDao competitionGroupSetDao;

    private Prerequisites prerequisites;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(RunHibernateEntity.class)
            .addEntityClass(PersonHibernateEntity.class)
            .addEntityClass(EventHibernateEntity.class)
            .addEntityClass(RegistrationHibernateEntity.class)
            .addEntityClass(HandicapGroupHibernateEntity.class)
            .addEntityClass(HandicapGroupSetHibernateEntity.class)
            .addEntityClass(CompetitionGroupHibernateEntity.class)
            .addEntityClass(CompetitionGroupSetHibernateEntity.class)
            .build();

    @Before
    public void setup() {
        SessionFactory sessionFactory = daoTestRule.getSessionFactory();
        dao = new RunDao(sessionFactory);
        eventDao = new EventDao(sessionFactory);
        registrationDao = new RegistrationDao(sessionFactory);
        handicapGroupDao = new HandicapGroupDao(sessionFactory);
        handicapGroupSetDao = new HandicapGroupSetDao(sessionFactory);
        competitionGroupDao = new CompetitionGroupDao(sessionFactory);
        competitionGroupSetDao = new CompetitionGroupSetDao(sessionFactory);

        prerequisites = setupPrerequisites();
    }

    @Test
    public void itShouldRoundTrip() {
        // create and store an entity
        RunHibernateEntity inEntity = buildUnsavedRun();
        daoTestRule.inTransaction(() -> dao.create(inEntity));

        // clear the cache
        daoTestRule.getSessionFactory().getCurrentSession().clear();

        // read a fresh instance of the stored entity
        RunHibernateEntity outEntity = daoTestRule.inTransaction(() -> dao.findById(inEntity.getId()));

        // assertions
        assertThat(outEntity)
                .isNotNull()
                .isNotSameAs(inEntity)
                .extracting(RunHibernateEntity::getId, RunHibernateEntity::getRawTime, RunHibernateEntity::getSequence)
                .containsExactly(inEntity.getId(), inEntity.getRawTime(), inEntity.getSequence());
    }

    @Test
    public void itShouldThrowWhenEventIsNull() {
        RunHibernateEntity entity = buildUnsavedRun();
        entity.setEvent(null);

        try {
            daoTestRule.inTransaction(() -> dao.create(entity));
            failBecauseExceptionWasNotThrown(PersistenceException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(PersistenceException.class)
                    .hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }

    @Test
    public void itShouldThrowWhenRawTimeOverflows() {
        RunHibernateEntity entity = buildUnsavedRun();
        entity.setRawTime(BigDecimal.valueOf(1000d));

        try {
            daoTestRule.inTransaction(() -> dao.create(entity));
            failBecauseExceptionWasNotThrown(PersistenceException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(PersistenceException.class)
                    .hasCauseInstanceOf(DataException.class);
        }
    }

    @Test
    public void itShouldThrowWhenSequenceLessThanOne() {
        RunHibernateEntity entity = buildUnsavedRun();
        entity.setSequence(0);

        try {
            daoTestRule.inTransaction(() -> dao.create(entity));
            failBecauseExceptionWasNotThrown(javax.validation.ConstraintViolationException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(javax.validation.ConstraintViolationException.class);
        }
    }

    @Test
    @Ignore("Deferring work on this, see https://github.com/caeos/coner-core/issues/181")
    public void itShouldThrowWhenEventAndSequenceNotUnique() throws InterruptedException {
        RunHibernateEntity entity1 = buildUnsavedRun();
        entity1.setSequence(1);
        RunHibernateEntity entity2 = buildUnsavedRun();
        entity2.setSequence(1);

        // sanity checks
        assertThat(entity1.getEvent()).isSameAs(entity2.getEvent());
        assertThat(entity1.getSequence()).isEqualTo(entity2.getSequence());

        try {
            daoTestRule.inTransaction(() -> {
                dao.create(entity1);
                dao.create(entity2);
                failBecauseExceptionWasNotThrown(PersistenceException.class);
            });
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(PersistenceException.class)
                    .hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }

    @Test
    public void itShouldInsertWithoutRegistration() {
        RunHibernateEntity entity = buildUnsavedRun();
        entity.setRegistration(null);

        daoTestRule.inTransaction(() -> dao.create(entity));
    }

    @Test
    public void itShouldInsertWithoutRawTime() {
        RunHibernateEntity entity = buildUnsavedRun();
        entity.setRawTime(null);

        daoTestRule.inTransaction(() -> dao.create(entity));
    }

    @Test
    public void itShouldInsertWithoutRegistrationAndRawTime() {
        RunHibernateEntity entity = buildUnsavedRun();
        entity.setRegistration(null);
        entity.setRawTime(null);

        daoTestRule.inTransaction(() -> dao.create(entity));
    }

    @Test
    public void itShouldFindLastInSequenceNullWhenNoneExists() {
        daoTestRule.inTransaction(() -> {
            RunHibernateEntity lastInSequenceForEvent = dao.findLastInSequenceFor(prerequisites.event);
            assertThat(lastInSequenceForEvent).isNull();
        });
    }

    @Test
    public void itShouldFindLastInSequenceCorrectWhenPriorRunsExist() {
        daoTestRule.inTransaction(() -> {
            // setup prior runs
            RunHibernateEntity entity1 = buildUnsavedRun();
            entity1.setTimestamp(Instant.now().minus(10, ChronoUnit.MINUTES));
            entity1.setSequence(1);
            RunHibernateEntity entity2 = buildUnsavedRun();
            entity2.setTimestamp(Instant.now().minus(5, ChronoUnit.MINUTES));
            entity2.setSequence(2);
            dao.create(entity1);
            dao.create(entity2);

            RunHibernateEntity actual = dao.findLastInSequenceFor(prerequisites.event);

            assertThat(actual)
                    .isNotNull()
                    .isSameAs(entity2);
        });
    }

    @Test
    public void itShouldFindFirstInSequenceWithoutRawTimeWhenExists() {
        daoTestRule.inTransaction(() -> {
            // setup three runs, only the first of which has a raw time
            RunHibernateEntity runWithTime = buildUnsavedRun();
            runWithTime.setSequence(1);
            RunHibernateEntity runWithoutTime1 = buildUnsavedRun();
            runWithoutTime1.setRawTime(null);
            runWithoutTime1.setSequence(2);
            RunHibernateEntity runWithoutTime2 = buildUnsavedRun();
            runWithoutTime2.setRawTime(null);
            runWithoutTime2.setSequence(3);
            dao.create(runWithTime);
            dao.create(runWithoutTime1);
            dao.create(runWithoutTime2);

            RunHibernateEntity actual = dao.findFirstInSequenceWithoutRawTime(prerequisites.event);

            assertThat(actual).isSameAs(runWithoutTime1);
        });
    }

    @Test
    public void whenAllRunsHaveRawTimesThenFirstInSequenceWithoutRawTimeShouldBeNull() {
        daoTestRule.inTransaction(() -> {
            // setup one run with a time
            RunHibernateEntity run = buildUnsavedRun();
            dao.create(run);

            RunHibernateEntity actual = dao.findFirstInSequenceWithoutRawTime(prerequisites.event);

            assertThat(actual).isNull();
        });
    }

    @Test
    public void whenNoRunsThenFirstInSequenceWithoutRawTimeShouldBeNull() {
        daoTestRule.inTransaction(() -> {
            // no runs

            RunHibernateEntity actual = dao.findFirstInSequenceWithoutRawTime(prerequisites.event);

            assertThat(actual).isNull();
        });
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
        prerequisites.registration = HibernateEntityTestUtils.fullRegistration();
        prerequisites.registration.setId(null);
        prerequisites.registration.getPerson().setId(null);
        prerequisites.registration.setEvent(prerequisites.event);
        prerequisites.registration.setHandicapGroup(prerequisites.handicapGroup);
        prerequisites.registration.setCompetitionGroup(prerequisites.competitionGroup);

        daoTestRule.inTransaction(() -> {
            handicapGroupDao.create(prerequisites.handicapGroup);
            handicapGroupSetDao.create(prerequisites.handicapGroupSet);
            competitionGroupDao.create(prerequisites.competitionGroup);
            competitionGroupSetDao.create(prerequisites.competitionGroupSet);
            eventDao.create(prerequisites.event);
            registrationDao.create(prerequisites.registration);
            // intentionally does not create prerequisites.unsavedRun
        });
        return prerequisites;
    }

    private static class Prerequisites {
        private EventHibernateEntity event;
        private HandicapGroupHibernateEntity handicapGroup;
        private HandicapGroupSetHibernateEntity handicapGroupSet;
        private CompetitionGroupHibernateEntity competitionGroup;
        private CompetitionGroupSetHibernateEntity competitionGroupSet;
        private RegistrationHibernateEntity registration;

    }

    private RunHibernateEntity buildUnsavedRun() {
        Preconditions.checkState(prerequisites != null, "first call setupPrerequisites!");
        RunHibernateEntity unsavedRun = HibernateEntityTestUtils.fullRun();
        unsavedRun.setId(null);
        unsavedRun.setEvent(prerequisites.event);
        unsavedRun.setRegistration(prerequisites.registration);
        return unsavedRun;
    }
}
