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
    MapStructAbstractGateway.Converter<TestDomainAddPayload, TestHibernateEntity>
            domainAddPayloadToHibernateEntityConverter;
    @Mock
    MapStructAbstractGateway.Merger<TestDomainEntity, TestHibernateEntity>
            domainEntityToHibernateEntityMerger;
    @Mock
    MapStructAbstractGateway.Converter<TestHibernateEntity, TestDomainEntity>
            hibernateEntityToDomainEntityConverter;
    @Mock
    MapStructAbstractGateway.Converter<List<TestHibernateEntity>, List<TestDomainEntity>>
            hibernateEntitiesToDomainEntitiesConverter;

    private TestMapStructAbstractGateway gateway;

    @Before
    public void setup() {
        gateway = new TestMapStructAbstractGateway(
                domainAddPayloadToHibernateEntityConverter,
                domainEntityToHibernateEntityMerger,
                hibernateEntityToDomainEntityConverter,
                hibernateEntitiesToDomainEntitiesConverter,
                dao
        );
    }

    @Test
    public void whenAdd() {
        TestDomainAddPayload domainAddPayload = mock(TestDomainAddPayload.class);
        TestHibernateEntity hibernateEntity = mock(TestHibernateEntity.class);
        when(domainAddPayloadToHibernateEntityConverter.map(domainAddPayload)).thenReturn(hibernateEntity);
        TestDomainEntity domainEntity = mock(TestDomainEntity.class);
        when(hibernateEntityToDomainEntityConverter.map(hibernateEntity)).thenReturn(domainEntity);

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
        when(hibernateEntitiesToDomainEntitiesConverter.map(hibernateEntities)).thenReturn(domainEntities);

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
        when(hibernateEntityToDomainEntityConverter.map(hibernateEntity)).thenReturn(domainEntity);

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
                Converter<TestDomainAddPayload, TestHibernateEntity> domainAddPayloadToHibernateEntityConverter,
                Merger<TestDomainEntity, TestHibernateEntity> domainEntityToHibernateEntityMerger,
                Converter<TestHibernateEntity, TestDomainEntity> hibernateEntityToDomainEntityConverter,
                Converter<List<TestHibernateEntity>, List<TestDomainEntity>> hibernateEntitiesToDomainEntitiesConverter,
                HibernateEntityDao<TestHibernateEntity> dao
        ) {
            super(
                    domainAddPayloadToHibernateEntityConverter,
                    domainEntityToHibernateEntityMerger,
                    hibernateEntityToDomainEntityConverter,
                    hibernateEntitiesToDomainEntitiesConverter,
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
