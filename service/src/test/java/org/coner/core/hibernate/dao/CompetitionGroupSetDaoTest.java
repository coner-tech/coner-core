package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.testing.junit.DAOTestRule;

public class CompetitionGroupSetDaoTest extends AbstractDaoTest {

    private CompetitionGroupSetDao setDao;
    private CompetitionGroupDao entityDao;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
            .addEntityClass(CompetitionGroupSetHibernateEntity.class)
            .addEntityClass(CompetitionGroupHibernateEntity.class)
            .build();

    @Before
    public void setup() {
        entityDao = new CompetitionGroupDao(daoTestRule.getSessionFactory());
        setDao = new CompetitionGroupSetDao(daoTestRule.getSessionFactory());
    }

    @Test
    /**
     * @see https://github.com/caeos/coner-core/issues/172
     */
    public void itShouldRoundTripWithoutLosingRelatedEntities() {
        // create and store an entity
        CompetitionGroupHibernateEntity inEntity = HibernateEntityTestUtils.fullCompetitionGroup();
        inEntity.setId(null); // not created yet
        daoTestRule.inTransaction(() -> entityDao.create(inEntity));

        // create and store a set with the entity
        CompetitionGroupSetHibernateEntity inSet = HibernateEntityTestUtils.fullCompetitionGroupSet();
        inSet.setId(null);
        inSet.setCompetitionGroups(Sets.newHashSet(inEntity));
        daoTestRule.inTransaction(() -> setDao.create(inSet));

        // clear the cache
        daoTestRule.getSessionFactory().getCurrentSession().clear();

        // read a fresh instance of the stored set
        CompetitionGroupSetHibernateEntity outSet = daoTestRule.inTransaction(
                () -> setDao.findById(inSet.getId())
        );

        // assert
        assertThat(outSet).isNotSameAs(inSet);
        assertThat(outSet.getCompetitionGroups()).hasSize(1);
    }
}
