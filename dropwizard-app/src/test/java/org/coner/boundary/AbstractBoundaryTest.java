package org.coner.boundary;

import org.coner.api.entity.ApiEntity;
import org.coner.core.domain.DomainEntity;
import org.coner.hibernate.entity.HibernateEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractBoundaryTest {

    private final Class<TestApiEntity> apiClass = TestApiEntity.class;
    private final Class<TestDomainEntity> domainClass = TestDomainEntity.class;
    private final Class<TestHibernateEntity> hibernateClass = TestHibernateEntity.class;
    private final Random random = new Random();
    private AbstractBoundary<TestApiEntity, TestDomainEntity, TestHibernateEntity> abstractBoundary;
    @Mock
    private EntityMerger<TestApiEntity, TestDomainEntity> apiToDomainMerger;
    @Mock
    private EntityMerger<TestDomainEntity, TestApiEntity> domainToApiMerger;
    @Mock
    private EntityMerger<TestDomainEntity, TestHibernateEntity> domainToHibernateMerger;
    @Mock
    private EntityMerger<TestHibernateEntity, TestDomainEntity> hibernateToDomainMerger;

    @Before
    public void setup() {
        abstractBoundary = new TestBoundary();
    }

    @Test
    public void whenConvertDomainNullToApiEntityItShouldReturnNull() {
        TestDomainEntity testDomainEntity = null;
        Object actual = abstractBoundary.toApiEntity(testDomainEntity);
        assertThat(actual).isNull();
    }

    @Test
    public void whenConvertDomainInstanceToApiEntityItShouldReturnInstance() {
        TestDomainEntity testDomainEntity = new TestDomainEntity();
        TestApiEntity actual = abstractBoundary.toApiEntity(testDomainEntity);
        assertThat(actual).isNotNull();
        verify(domainToApiMerger).merge(testDomainEntity, actual);
    }

    @Test
    public void whenConvertApiNullToDomainEntityItShouldReturnNull() {
        TestApiEntity testApiEntity = null;
        TestDomainEntity actual = abstractBoundary.toDomainEntity(testApiEntity);
        assertThat(actual).isNull();
        verify(apiToDomainMerger, never()).merge(any(), any());
    }

    @Test
    public void whenConvertApiInstanceToDomainEntityItShouldReturnInstance() {
        TestApiEntity apiEntity = new TestApiEntity();
        TestDomainEntity actual = abstractBoundary.toDomainEntity(apiEntity);
        assertThat(actual).isNotNull();
        verify(apiToDomainMerger).merge(apiEntity, actual);
    }

    @Test
    public void whenConvertHibernateNullToDomainEntityItShouldReturnNull() {
        TestHibernateEntity testHibernateEntity = null;
        TestDomainEntity actual = abstractBoundary.toDomainEntity(testHibernateEntity);
        assertThat(actual).isNull();
        verify(hibernateToDomainMerger, never()).merge(any(), any());
    }

    @Test
    public void whenConvertHibernateInstanceToDomainEntityItShouldReturnInstance() {
        TestHibernateEntity testHibernateEntity = new TestHibernateEntity();
        TestDomainEntity actual = abstractBoundary.toDomainEntity(testHibernateEntity);
        assertThat(actual).isNotNull();
        verify(hibernateToDomainMerger).merge(testHibernateEntity, actual);
    }

    @Test
    public void whenConvertDomainNullToHibernateEntityItShouldReturnNull() {
        TestDomainEntity testDomainEntity = null;
        TestHibernateEntity actual = abstractBoundary.toHibernateEntity(testDomainEntity);
        assertThat(actual).isNull();
        verify(domainToHibernateMerger, never()).merge(any(), any());
    }

    @Test
    public void whenConvertDomainInstanceToHibernateEntityItShouldReturnInstance() {
        TestDomainEntity testDomainEntity = new TestDomainEntity();
        TestHibernateEntity actual = abstractBoundary.toHibernateEntity(testDomainEntity);
        assertThat(actual).isNotNull();
        verify(domainToHibernateMerger).merge(testDomainEntity, actual);
    }

    @Test
    public void whenConvertListDomainEntitiesToApiEntitiesItShouldConvert() {
        final int size = 1 + random.nextInt(5);
        List<TestDomainEntity> testDomainEntities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            testDomainEntities.add(new TestDomainEntity());
        }
        List<TestApiEntity> actual = abstractBoundary.toApiEntities(testDomainEntities);
        assertThat(actual)
                .isNotNull()
                .hasSize(size);
        verify(domainToApiMerger, times(size)).merge(any(domainClass), any(apiClass));
    }

    @Test
    public void whenConvertListDomainNullToApiEntitiesItShouldReturnEmpty() {
        List<TestDomainEntity> testDomainEntities = null;
        List<TestApiEntity> actual = abstractBoundary.toApiEntities(testDomainEntities);
        assertThat(actual)
                .isNotNull()
                .isEmpty();
        verify(domainToApiMerger, never()).merge(any(domainClass), any(apiClass));
    }

    @Test
    public void whenConvertListHibernateEntitiesToDomainEntitiesItShouldConvert() {
        final int size = 1 + random.nextInt(5);
        List<TestHibernateEntity> testHibernateEntities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            testHibernateEntities.add(new TestHibernateEntity());
        }
        List<TestDomainEntity> actual = abstractBoundary.toDomainEntities(testHibernateEntities);
        assertThat(actual)
                .isNotNull()
                .hasSize(size);
        verify(hibernateToDomainMerger, times(size)).merge(any(hibernateClass), any(domainClass));
    }

    @Test
    public void whenConvertListHibernateNullToDomainEntitiesItShouldReturnEmpty() {
        List<TestHibernateEntity> testHibernateEntities = null;
        List<TestDomainEntity> actual = abstractBoundary.toDomainEntities(testHibernateEntities);
        assertThat(actual)
                .isNotNull()
                .isEmpty();
        verify(hibernateToDomainMerger, never()).merge(any(hibernateClass), any(domainClass));
    }

    @Test
    public void whenConvertListDomainEntitiesToHibernateEntitiesItShouldConvert() {
        final int size = 1 + random.nextInt(5);
        List<TestDomainEntity> testDomainEntities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            testDomainEntities.add(new TestDomainEntity());
        }
        List<TestHibernateEntity> actual = abstractBoundary.toHibernateEntities(testDomainEntities);
        assertThat(actual)
                .isNotNull()
                .hasSize(size);
        verify(domainToHibernateMerger, times(size)).merge(any(domainClass), any(hibernateClass));
    }

    @Test
    public void whenConvertListDomainNullToHibernateEntitiesItShouldReturnEmpty() {
        List<TestDomainEntity> testDomainEntities = null;
        List<TestHibernateEntity> actual = abstractBoundary.toHibernateEntities(testDomainEntities);
        assertThat(actual)
                .isNotNull()
                .isEmpty();
        verify(domainToHibernateMerger, never()).merge(any(domainClass), any(hibernateClass));
    }

    @Test
    public void whenMergeApiIntoDomainItShouldMerge() {
        TestApiEntity testApiEntity = new TestApiEntity();
        TestDomainEntity testDomainEntity = new TestDomainEntity();

        abstractBoundary.merge(testApiEntity, testDomainEntity);

        verify(apiToDomainMerger).merge(testApiEntity, testDomainEntity);
    }

    @Test
    public void whenMergeNullApiIntoDomainItShouldNpe() {
        try {
            TestApiEntity testApiEntity = null;
            TestDomainEntity testDomainEntity = new TestDomainEntity();
            abstractBoundary.merge(testApiEntity, testDomainEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(apiToDomainMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeApiIntoNullDomainItShouldNpe() {
        try {
            TestApiEntity testApiEntity = new TestApiEntity();
            TestDomainEntity testDomainEntity = null;
            abstractBoundary.merge(testApiEntity, testDomainEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(apiToDomainMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeDomainIntoApiItShouldMerge() {
        TestDomainEntity testDomainEntity = new TestDomainEntity();
        TestApiEntity testApiEntity = new TestApiEntity();

        abstractBoundary.merge(testDomainEntity, testApiEntity);

        verify(domainToApiMerger).merge(testDomainEntity, testApiEntity);
    }

    @Test
    public void whenMergeNullDomainIntoApiItShouldNpe() {
        try {
            TestDomainEntity testDomainEntity = null;
            TestApiEntity testApiEntity = new TestApiEntity();
            abstractBoundary.merge(testDomainEntity, testApiEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(domainToApiMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeDomainIntoNullApiItShouldNpe() {
        try {
            TestDomainEntity testDomainEntity = new TestDomainEntity();
            TestApiEntity testApiEntity = null;
            abstractBoundary.merge(testDomainEntity, testApiEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(domainToApiMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeDomainIntoHibernateItShouldMerge() {
        TestDomainEntity testDomainEntity = new TestDomainEntity();
        TestHibernateEntity testHibernateEntity = new TestHibernateEntity();

        abstractBoundary.merge(testDomainEntity, testHibernateEntity);

        verify(domainToHibernateMerger).merge(testDomainEntity, testHibernateEntity);
    }

    @Test
    public void whenMergeNullDomainIntoHibernateItShouldNpe() {
        try {
            TestDomainEntity testDomainEntity = null;
            TestHibernateEntity testHibernateEntity = new TestHibernateEntity();
            abstractBoundary.merge(testDomainEntity, testHibernateEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(domainToHibernateMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeDomainIntoNullHibernateItShouldNpe() {
        try {
            TestDomainEntity testDomainEntity = new TestDomainEntity();
            TestHibernateEntity testHibernateEntity = null;
            abstractBoundary.merge(testDomainEntity, testHibernateEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(domainToHibernateMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeHibernateIntoDomainItShouldMerge() {
        TestHibernateEntity testHibernateEntity = new TestHibernateEntity();
        TestDomainEntity testDomainEntity = new TestDomainEntity();

        abstractBoundary.merge(testHibernateEntity, testDomainEntity);

        verify(hibernateToDomainMerger).merge(testHibernateEntity, testDomainEntity);
    }

    @Test
    public void whenMergeNullHibernateIntoDomainItShouldNpe() {
        try {
            TestHibernateEntity testHibernateEntity = null;
            TestDomainEntity testDomainEntity = new TestDomainEntity();
            abstractBoundary.merge(testHibernateEntity, testDomainEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(hibernateToDomainMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeHibernateIntoNullDomainItShouldNpe() {
        try {
            TestHibernateEntity testHibernateEntity = new TestHibernateEntity();
            TestDomainEntity testDomainEntity = null;
            abstractBoundary.merge(testHibernateEntity, testDomainEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(hibernateToDomainMerger, never()).merge(any(), any());
    }

    @Test
    public void whenInstantiateEntityWithPrivateNoArgConstructorItShouldThrow() {
        try {
            abstractBoundary.instantiate(PrivateConstructorEntity.class);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(RuntimeException.class);
            assertThat(e.getCause())
                    .isInstanceOf(NoSuchMethodException.class);
        }
    }

    @Test
    public void whenInstantiateEntityConstructorThrowsItShouldThrow() {
        try {
            abstractBoundary.instantiate(ExceptionThrowingConstructorEntity.class);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(RuntimeException.class);
            assertThat(e.getCause())
                    .isInstanceOf(InvocationTargetException.class);
        }
    }

    @Test
    public void whenInstantiateEntityAbstractItShouldThrow() {
        try {
            abstractBoundary.instantiate(AbstractEntity.class);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(RuntimeException.class);
            assertThat(e.getCause())
                    .isInstanceOf(InstantiationException.class);
        }
    }

    public static class TestApiEntity extends ApiEntity {
    }

    public static class TestDomainEntity extends DomainEntity {
    }

    public static class TestHibernateEntity extends HibernateEntity {
    }

    public static final class PrivateConstructorEntity {
        private PrivateConstructorEntity() {
        }
    }

    public static class ExceptionThrowingConstructorEntity {
        public ExceptionThrowingConstructorEntity() throws Exception {
            throw new Exception();
        }
    }

    public abstract static class AbstractEntity {
    }

    private class TestBoundary extends AbstractBoundary<TestApiEntity, TestDomainEntity, TestHibernateEntity> {

        @Override
        protected EntityMerger<TestApiEntity, TestDomainEntity> buildApiToDomainMerger() {
            return apiToDomainMerger;
        }

        @Override
        protected EntityMerger<TestDomainEntity, TestApiEntity> buildDomainToApiMerger() {
            return domainToApiMerger;
        }

        @Override
        protected EntityMerger<TestDomainEntity, TestHibernateEntity> buildDomainToHibernateMerger() {
            return domainToHibernateMerger;
        }

        @Override
        protected EntityMerger<TestHibernateEntity, TestDomainEntity> buildHibernateToDomainMerger() {
            return hibernateToDomainMerger;
        }
    }

}
