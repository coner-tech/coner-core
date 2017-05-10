package org.coner.core.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.coner.core.domain.entity.DomainEntity;
import org.coner.core.domain.payload.DomainAddPayload;
import org.coner.core.hibernate.dao.HibernateEntityDao;
import org.coner.core.hibernate.entity.HibernateEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapStructAbstractGatewayTest {

    @Mock
    HibernateEntityDao<TestHibernateEntity> dao;
    @Mock
    MapStructAbstractGateway.Mapper<TestDomainAddPayload, TestHibernateEntity>
            domainAddPayloadToHibernateEntityMapper;
    @Mock
    MapStructAbstractGateway.Mapper<TestHibernateEntity, TestDomainEntity>
            hibernateEntityToDomainEntityMapper;
    @Mock
    MapStructAbstractGateway.Mapper<List<TestHibernateEntity>, List<TestDomainEntity>>
            hibernateEntitiesToDomainEntitiesMapper;

    private TestMapStructAbstractGateway gateway;

    @Before
    public void setup() {
        gateway = new TestMapStructAbstractGateway(
                domainAddPayloadToHibernateEntityMapper,
                hibernateEntityToDomainEntityMapper,
                hibernateEntitiesToDomainEntitiesMapper,
                dao
        );
    }

    @Test
    public void whenAdd() {
        TestDomainAddPayload domainAddPayload = mock(TestDomainAddPayload.class);
        TestHibernateEntity hibernateEntity = mock(TestHibernateEntity.class);
        when(domainAddPayloadToHibernateEntityMapper.map(domainAddPayload)).thenReturn(hibernateEntity);
        TestDomainEntity domainEntity = mock(TestDomainEntity.class);
        when(hibernateEntityToDomainEntityMapper.map(hibernateEntity)).thenReturn(domainEntity);

        TestDomainEntity actual = gateway.add(domainAddPayload);

        verify(dao).create(hibernateEntity);
        verifyNoMoreInteractions(dao);
        assertThat(actual).isSameAs(domainEntity);
    }

    @Test
    public void whenAddNullPayload() throws Exception {
        try {
            gateway.add(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            verifyZeroInteractions(dao);
        }
    }

    @Test
    public void whenGetAll() {
        List<TestHibernateEntity> hibernateEntities = mock(List.class);
        when(dao.findAll()).thenReturn(hibernateEntities);
        List<TestDomainEntity> domainEntities = mock(List.class);
        when(hibernateEntitiesToDomainEntitiesMapper.map(hibernateEntities)).thenReturn(domainEntities);

        List<TestDomainEntity> actual = gateway.getAll();

        verify(dao).findAll();
        verifyNoMoreInteractions(dao);
        assertThat(actual).isSameAs(domainEntities);
    }

    @Test
    public void whenFindById() {
        final String testId = "test.id";
        TestHibernateEntity hibernateEntity = mock(TestHibernateEntity.class);
        when(dao.findById(testId)).thenReturn(hibernateEntity);
        TestDomainEntity domainEntity = mock(TestDomainEntity.class);
        when(hibernateEntityToDomainEntityMapper.map(hibernateEntity)).thenReturn(domainEntity);

        TestDomainEntity actual = gateway.findById(testId);

        verify(dao).findById(testId);
        assertThat(actual).isSameAs(domainEntity);
    }

    private static class TestMapStructAbstractGateway extends MapStructAbstractGateway<
            TestDomainAddPayload,
            TestDomainEntity,
            TestHibernateEntity,
            HibernateEntityDao<TestHibernateEntity>
            > {

        protected TestMapStructAbstractGateway(
                Mapper<TestDomainAddPayload, TestHibernateEntity> domainAddPayloadToHibernateEntityMapper,
                Mapper<TestHibernateEntity, TestDomainEntity> hibernateEntityToDomainEntityMapper,
                Mapper<List<TestHibernateEntity>, List<TestDomainEntity>> hibernateEntitiesToDomainEntitiesMapper,
                HibernateEntityDao<TestHibernateEntity> dao
        ) {
            super(
                    domainAddPayloadToHibernateEntityMapper,
                    hibernateEntityToDomainEntityMapper,
                    hibernateEntitiesToDomainEntitiesMapper,
                    dao
            );
        }
    }

    private static class TestDomainAddPayload extends DomainAddPayload {

    }

    private static class TestDomainEntity extends DomainEntity {

    }

    private static class TestHibernateEntity extends HibernateEntity {

    }

}
