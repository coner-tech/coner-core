package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.DAOTestRule;

public class HandicapGroupsDaoTest extends AbstractDaoTest {

    private HandicapGroupDao handicapGroupDao;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(HandicapGroupHibernateEntity.class)
            .addEntityClass(HandicapGroupSetHibernateEntity.class)
            .build();

    @Before
    public void setup() {
        handicapGroupDao = new HandicapGroupDao(daoTestRule.getSessionFactory());
    }

    @Test
    public void itShouldRoundTripWithoutFactorLosingPrecision() {
        // create and store an entity
        HandicapGroupHibernateEntity inEntity = HibernateEntityTestUtils.fullHandicapGroup();
        inEntity.setId(null); // not created yet
        daoTestRule.inTransaction(() -> handicapGroupDao.create(inEntity));

        // clear the cache
        daoTestRule.getSessionFactory().getCurrentSession().clear();

        // read a fresh instance of the stored entity
        HandicapGroupHibernateEntity outEntity = daoTestRule.inTransaction(
                () -> handicapGroupDao.findById(inEntity.getId())
        );

        // assert
        assertThat(outEntity).isNotSameAs(inEntity);
        assertThat(outEntity.getHandicapFactor()).isEqualTo(TestConstants.HANDICAP_GROUP_FACTOR);
    }
}
