package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.coner.core.domain.entity.CompetitionGroup;
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


    @Test
    public void itShouldSaveMultipleSetsAssociatedToSameCompetitionGroup() {
        daoTestRule.inTransaction(() -> {
            // create and store an entity
            CompetitionGroupHibernateEntity entity = HibernateEntityTestUtils.fullCompetitionGroup();
            entity.setId(null);
            entityDao.create(entity);

            // create and store a set1
            CompetitionGroupSetHibernateEntity setEntity1 = HibernateEntityTestUtils.fullCompetitionGroupSet();
            setEntity1.setId(null);
            setEntity1.setCompetitionGroups(Sets.newHashSet());
            setDao.create(setEntity1);

            // create and store a set2
            CompetitionGroupSetHibernateEntity setEntity2 = HibernateEntityTestUtils.fullCompetitionGroupSet();
            setEntity2.setId(null);
            setEntity2.setCompetitionGroups(Sets.newHashSet());
            setDao.create(setEntity2);

            assertThat(setEntity1.getId()).isNotEqualTo(setEntity2.getId()); // sanity check

            // add entity to both sets and update
            setEntity1.getCompetitionGroups().add(entity);
            setEntity2.getCompetitionGroups().add(entity);
            setDao.update(setEntity1);
            setDao.update(setEntity2);
        });
    }

    @Test
    public void itShouldSaveOneSetAssociatedtoMultipleCompetitionGroups() {
        daoTestRule.inTransaction(() -> {
            // create and store entity1
            CompetitionGroupHibernateEntity entity1 = HibernateEntityTestUtils.fullCompetitionGroup();
            entity1.setId(null);
            entityDao.create(entity1);

            // create and store entity2
            CompetitionGroupHibernateEntity entity2 = HibernateEntityTestUtils.fullCompetitionGroup();
            entity2.setId(null);
            entityDao.create(entity2);

            assertThat(entity1.getId()).isNotEqualTo(entity2.getId()); // sanity check

            // add both entities to set and update
            CompetitionGroupSetHibernateEntity setEntity = HibernateEntityTestUtils.fullCompetitionGroupSet();
            setEntity.setId(null);
            setEntity.setCompetitionGroups(Sets.newHashSet(entity1, entity2));
            setDao.create(setEntity);
        });
    }

    @Test
    public void itShouldCreateWithMultipleCompetitionGroups() {
        daoTestRule.inTransaction(() -> {
            // create entities
            CompetitionGroupHibernateEntity openEntity = HibernateEntityTestUtils.fullCompetitionGroup(
                    null,
                    "Open",
                    false,
                    BigDecimal.ONE,
                    CompetitionGroup.ResultTimeType.RAW.toString()
            );
            entityDao.create(openEntity);
            CompetitionGroupHibernateEntity noviceEntity = HibernateEntityTestUtils.fullCompetitionGroup(
                    null,
                    "Novice",
                    true,
                    BigDecimal.ONE,
                    CompetitionGroup.ResultTimeType.HANDICAP.toString()
            );
            entityDao.create(noviceEntity);
            CompetitionGroupHibernateEntity proEntity = HibernateEntityTestUtils.fullCompetitionGroup(
                    null,
                    "Pro",
                    true,
                    BigDecimal.ONE,
                    CompetitionGroup.ResultTimeType.HANDICAP.toString()
            );
            entityDao.create(proEntity);

            // create set with multiple competition groups
            CompetitionGroupSetHibernateEntity setEntity = HibernateEntityTestUtils.fullCompetitionGroupSet(
                    null,
                    "2017 Competition Groups",
                    Sets.newHashSet(openEntity, noviceEntity, proEntity)
            );
            setDao.create(setEntity);
        });
    }
}
