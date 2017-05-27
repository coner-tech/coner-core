package org.coner.core.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

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

    @Test
    public void itShouldSaveMultipleSetsAssociatedToSameHandicapGroup() {
        daoTestRule.inTransaction(() -> {
            // create and store an entity
            HandicapGroupHibernateEntity entity = HibernateEntityTestUtils.fullHandicapGroup();
            entity.setId(null);
            entityDao.create(entity);

            // create and store a set1
            HandicapGroupSetHibernateEntity setEntity1 = HibernateEntityTestUtils.fullHandicapGroupSet();
            setEntity1.setId(null);
            setEntity1.setHandicapGroups(Sets.newHashSet());
            setDao.create(setEntity1);

            // create and store a set2
            HandicapGroupSetHibernateEntity setEntity2 = HibernateEntityTestUtils.fullHandicapGroupSet();
            setEntity2.setId(null);
            setEntity2.setHandicapGroups(Sets.newHashSet());
            setDao.create(setEntity2);

            assertThat(setEntity1.getId()).isNotEqualTo(setEntity2.getId()); // sanity check

            // add entity to both sets and update
            setEntity1.getHandicapGroups().add(entity);
            setEntity2.getHandicapGroups().add(entity);
            setDao.update(setEntity1);
            setDao.update(setEntity2);
        });
    }

    @Test
    public void itShouldSaveOneSetAssociatedtoMultipleHandicapGroups() {
        daoTestRule.inTransaction(() -> {
            // create and store entity1
            HandicapGroupHibernateEntity entity1 = HibernateEntityTestUtils.fullHandicapGroup();
            entity1.setId(null);
            entityDao.create(entity1);

            // create and store entity2
            HandicapGroupHibernateEntity entity2 = HibernateEntityTestUtils.fullHandicapGroup();
            entity2.setId(null);
            entityDao.create(entity2);

            assertThat(entity1.getId()).isNotEqualTo(entity2.getId()); // sanity check

            // add both entities to set and update
            HandicapGroupSetHibernateEntity setEntity = HibernateEntityTestUtils.fullHandicapGroupSet();
            setEntity.setId(null);
            setEntity.setHandicapGroups(Sets.newHashSet(entity1, entity2));
            setDao.create(setEntity);
        });
    }

    @Test
    public void itShouldCreateWithMultipleHandicapGroups() {
        daoTestRule.inTransaction(() -> {
            // create entities
            HandicapGroupHibernateEntity ssEntity = HibernateEntityTestUtils.fullHandicapGroup(
                    null,
                    "SS",
                    BigDecimal.valueOf(0.826d)
            );
            entityDao.create(ssEntity);
            HandicapGroupHibernateEntity asEntity = HibernateEntityTestUtils.fullHandicapGroup(
                    null,
                    "AS",
                    BigDecimal.valueOf(0.819d)
            );
            entityDao.create(asEntity);
            HandicapGroupHibernateEntity bsEntity = HibernateEntityTestUtils.fullHandicapGroup(
                    null,
                    "BS",
                    BigDecimal.valueOf(0.813d)
            );
            entityDao.create(bsEntity);

            // create set with multiple handicap groups
            HandicapGroupSetHibernateEntity setEntity = HibernateEntityTestUtils.fullHandicapGroupSet(
                    null,
                    "2017 PAX/RTP INDEX",
                    Sets.newHashSet(ssEntity, asEntity, bsEntity)
            );
            setDao.create(setEntity);
        });
    }
}
