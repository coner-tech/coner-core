package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.DAOTestRule;

public class CompetitionGroupDaoTest extends AbstractDaoTest {

    private CompetitionGroupDao dao;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(CompetitionGroupHibernateEntity.class)
            .addEntityClass(CompetitionGroupSetHibernateEntity.class)
            .build();

    @Before
    public void setup() {
        dao = new CompetitionGroupDao(daoTestRule.getSessionFactory());
    }

    @Test
    public void itShouldRoundTripWithoutFactorLosingPrecision() {
        // create and store an entity
        CompetitionGroupHibernateEntity inEntity = HibernateEntityTestUtils.fullCompetitionGroup();
        inEntity.setId(null); // not created yet
        daoTestRule.inTransaction(() -> dao.create(inEntity));

        // clear the cache
        daoTestRule.getSessionFactory().getCurrentSession().clear();

        // read a fresh instance of the stored entity
        CompetitionGroupHibernateEntity outEntity = daoTestRule.inTransaction(
                () -> dao.findById(inEntity.getId())
        );

        // assert
        assertThat(outEntity).isNotSameAs(inEntity);
        assertThat(outEntity.getFactor()).isEqualTo(TestConstants.COMPETITION_GROUP_FACTOR);
    }
}
