package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.testing.junit.DAOTestRule;

public class EventDaoTest extends AbstractDaoTest {

    private EventDao dao;
    private HandicapGroupDao handicapGroupDao;
    private HandicapGroupSetDao handicapGroupSetDao;

    private Prerequisites prerequisites;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(EventHibernateEntity.class)
            .addEntityClass(RegistrationHibernateEntity.class)
            .addEntityClass(HandicapGroupHibernateEntity.class)
            .addEntityClass(HandicapGroupSetHibernateEntity.class)
        .build();

    @Before
    public void setup() {
        SessionFactory sessionFactory = daoTestRule.getSessionFactory();
        dao = new EventDao(sessionFactory);
        handicapGroupDao = new HandicapGroupDao(sessionFactory);
        handicapGroupSetDao = new HandicapGroupSetDao(sessionFactory);
        prerequisites = setupPrerequisites();
    }

    @Test
    public void whenFindAllItShouldReturnEmpty() {
        daoTestRule.inTransaction(() -> {
            List<EventHibernateEntity> actual = dao.findAll();
            assertThat(actual)
                    .isNotNull()
                    .isEmpty();
        });
    }

    @Test
    public void whenCreateItShouldCreateEvent() {
        EventHibernateEntity newEvent = buildUnsavedEvent();

        daoTestRule.inTransaction(() -> {
            dao.create(newEvent);
        });

        assertThat(newEvent.getId())
                .isNotEmpty();
    }

    @Test
    public void whenCreateWithoutHandicapGroupSetItShouldThrow() {
        EventHibernateEntity entity = buildUnsavedEvent();
        entity.setHandicapGroupSet(null);

        try {
            daoTestRule.inTransaction(() -> dao.create(entity));

            failBecauseExceptionWasNotThrown(ConstraintViolationException.class);
        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("handicapGroupSet")
                    .containsIgnoringCase("null");
        }
    }

    @Test
    public void whenCreatedItShouldFindById() {
        EventHibernateEntity newEvent = buildUnsavedEvent();
        daoTestRule.inTransaction(() -> {
            dao.create(newEvent);
        });

        EventHibernateEntity actual = dao.findById(newEvent.getId());

        assertThat(actual)
                .isNotNull()
                .isEqualTo(newEvent);
    }

    @Test
    public void whenCreateItShouldBeInFindAll() {
        EventHibernateEntity newEvent = buildUnsavedEvent();
        daoTestRule.inTransaction(() -> {
            dao.create(newEvent);
        });

        List<EventHibernateEntity> events = dao.findAll();

        assertThat(events)
                .isNotNull()
                .isNotEmpty()
                .containsOnly(newEvent);
    }

    private Prerequisites setupPrerequisites() {
        Prerequisites prerequisites = new Prerequisites();
        Set<HandicapGroupHibernateEntity> handicapGroups2017 = Sets.newHashSet(
                HibernateEntityTestUtils.fullHandicapGroup(null, "SS", BigDecimal.valueOf(0.826)),
                HibernateEntityTestUtils.fullHandicapGroup(null, "AS", BigDecimal.valueOf(0.813)),
                HibernateEntityTestUtils.fullHandicapGroup(null, "BS", BigDecimal.valueOf(0.813))
        );
        HandicapGroupSetHibernateEntity handicapGroupSet2017 = HibernateEntityTestUtils.fullHandicapGroupSet(
                null, "2017 RTP/PAX Index", handicapGroups2017
        );
        prerequisites.handicapGroupSet = handicapGroupSet2017;
        daoTestRule.inTransaction(() -> {
            handicapGroups2017.forEach(handicapGroup -> handicapGroupDao.create(handicapGroup));
            handicapGroupSetDao.create(handicapGroupSet2017);
        });
        return prerequisites;
    }

    private static class Prerequisites {
        private HandicapGroupSetHibernateEntity handicapGroupSet;
    }

    private EventHibernateEntity buildUnsavedEvent() {
        EventHibernateEntity event = HibernateEntityTestUtils.fullEvent();
        event.setId(null);
        event.setHandicapGroupSet(prerequisites.handicapGroupSet);
        return event;
    }
}
