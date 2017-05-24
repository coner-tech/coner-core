package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.testing.junit.DAOTestRule;

public class HandicapGroupSetDaoTest extends AbstractDaoTest {

    private HandicapGroupSetDao setDao;
    private HandicapGroupDao entityDao;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(HandicapGroupSetHibernateEntity.class)
            .addEntityClass(HandicapGroupHibernateEntity.class)
            .build();

    @Before
    public void setup() {
        entityDao = new HandicapGroupDao(daoTestRule.getSessionFactory());
        setDao = new HandicapGroupSetDao(daoTestRule.getSessionFactory());
    }

    @Test
    /**
     * @see https://github.com/caeos/coner-core/issues/157
     */
    public void itShouldRoundTripWithoutLosingRelatedEntities() {
        // create and store an entity
        HandicapGroupHibernateEntity inEntity = HibernateEntityTestUtils.fullHandicapGroup();
        inEntity.setId(null); // not created yet
        daoTestRule.inTransaction(() -> entityDao.create(inEntity));

        // create and store a set with the entity
        HandicapGroupSetHibernateEntity inSet = HibernateEntityTestUtils.fullHandicapGroupSet();
        inSet.setId(null);
        inSet.setHandicapGroups(Sets.newHashSet(inEntity));
        daoTestRule.inTransaction(() -> setDao.create(inSet));

        // clear the cache
        daoTestRule.getSessionFactory().getCurrentSession().clear();

        // read a fresh instance of the stored set
        HandicapGroupSetHibernateEntity outSet = daoTestRule.inTransaction(
                () -> setDao.findById(inSet.getId())
        );

        // assert
        assertThat(outSet).isNotSameAs(inSet);
        assertThat(outSet.getHandicapGroups()).hasSize(1);
    }
}
