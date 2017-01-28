package org.coner.core.boundary;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.coner.core.api.entity.ApiEntity;
import org.coner.core.domain.entity.DomainEntity;
import org.coner.core.hibernate.entity.HibernateEntity;
import org.coner.core.util.merger.ObjectMerger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractBoundaryTest {

    private final Class<TestLocalEntity> localClass = TestLocalEntity.class;
    private final Class<TestRemoteEntity> remoteClass = TestRemoteEntity.class;
    private final Random random = new Random();
    private AbstractBoundary<TestLocalEntity, TestRemoteEntity> abstractBoundary;
    @Mock
    private ObjectMerger<TestLocalEntity, TestRemoteEntity> localToRemoteMerger;
    @Mock
    private ObjectMerger<TestRemoteEntity, TestLocalEntity> remoteToLocalMerger;

    @Before
    public void setup() {
        abstractBoundary = new TestBoundary();
    }

    @Test
    public void whenConvertRemoteNullToLocalEntityItShouldReturnNull() {
        TestRemoteEntity testRemoteEntity = null;
        Object actual = abstractBoundary.toLocalEntity(testRemoteEntity);
        assertThat(actual).isNull();
    }

    @Test
    public void whenConvertRemoteInstanceToLocalEntityItShouldReturnInstance() {
        TestRemoteEntity testRemoteEntity = new TestRemoteEntity();
        TestLocalEntity actual = abstractBoundary.toLocalEntity(testRemoteEntity);
        assertThat(actual).isNotNull();
        verify(remoteToLocalMerger).merge(testRemoteEntity, actual);
    }

    @Test
    public void whenConvertLocalNullToRemoteEntityItShouldReturnNull() {
        TestLocalEntity testLocalEntity = null;
        TestRemoteEntity actual = abstractBoundary.toRemoteEntity(testLocalEntity);
        assertThat(actual).isNull();
        verify(localToRemoteMerger, never()).merge(any(), any());
    }

    @Test
    public void whenConvertLocalInstanceToRemoteEntityItShouldReturnInstance() {
        TestLocalEntity testLocalEntity = new TestLocalEntity();
        TestRemoteEntity actual = abstractBoundary.toRemoteEntity(testLocalEntity);
        assertThat(actual).isNotNull();
        verify(localToRemoteMerger).merge(testLocalEntity, actual);
    }

    @Test
    public void whenConvertListRemoteEntitiesToLocalEntitiesItShouldConvert() {
        final int size = 1 + random.nextInt(5);
        List<TestRemoteEntity> testRemoteEntities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            testRemoteEntities.add(new TestRemoteEntity());
        }
        List<TestLocalEntity> actual = abstractBoundary.toLocalEntities(testRemoteEntities);
        assertThat(actual)
                .isNotNull()
                .hasSize(size);
        verify(remoteToLocalMerger, times(size)).merge(any(remoteClass), any(localClass));
    }

    @Test
    public void whenConvertListRemoteNullToLocalEntitiesItShouldReturnEmpty() {
        List<TestRemoteEntity> testRemoteEntities = null;
        List<TestLocalEntity> actual = abstractBoundary.toLocalEntities(testRemoteEntities);
        assertThat(actual)
                .isNotNull()
                .isEmpty();
        verify(remoteToLocalMerger, never()).merge(any(remoteClass), any(localClass));
    }

    @Test
    public void whenMergeLocalIntoRemoteItShouldMerge() {
        TestLocalEntity testLocalEntity = new TestLocalEntity();
        TestRemoteEntity testRemoteEntity = new TestRemoteEntity();

        abstractBoundary.mergeLocalIntoRemote(testLocalEntity, testRemoteEntity);

        verify(localToRemoteMerger).merge(testLocalEntity, testRemoteEntity);
    }

    @Test
    public void whenMergeNullLocalIntoRemoteItShouldNpe() {
        try {
            TestLocalEntity testLocalEntity = null;
            TestRemoteEntity testRemoteEntity = new TestRemoteEntity();
            abstractBoundary.mergeLocalIntoRemote(testLocalEntity, testRemoteEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(localToRemoteMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeLocalIntoNullRemoteItShouldNpe() {
        try {
            TestLocalEntity testLocalEntity = new TestLocalEntity();
            TestRemoteEntity testRemoteEntity = null;
            abstractBoundary.mergeLocalIntoRemote(testLocalEntity, testRemoteEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(localToRemoteMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeRemoteIntoLocalItShouldMerge() {
        TestRemoteEntity testRemoteEntity = new TestRemoteEntity();
        TestLocalEntity testLocalEntity = new TestLocalEntity();

        abstractBoundary.mergeRemoteIntoLocal(testRemoteEntity, testLocalEntity);

        verify(remoteToLocalMerger).merge(testRemoteEntity, testLocalEntity);
    }

    @Test
    public void whenMergeNullRemoteIntoLocalItShouldNpe() {
        try {
            TestRemoteEntity testRemoteEntity = null;
            TestLocalEntity testLocalEntity = new TestLocalEntity();
            abstractBoundary.mergeRemoteIntoLocal(testRemoteEntity, testLocalEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(remoteToLocalMerger, never()).merge(any(), any());
    }

    @Test
    public void whenMergeRemoteIntoNullLocalItShouldNpe() {
        try {
            TestRemoteEntity testRemoteEntity = new TestRemoteEntity();
            TestLocalEntity testLocalEntity = null;
            abstractBoundary.mergeRemoteIntoLocal(testRemoteEntity, testLocalEntity);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(true).isTrue();
        }
        verify(remoteToLocalMerger, never()).merge(any(), any());
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

    public static class TestLocalEntity extends ApiEntity {
    }

    public static class TestRemoteEntity extends DomainEntity {
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

    private class TestBoundary extends AbstractBoundary<TestLocalEntity, TestRemoteEntity> {

        @Override
        protected ObjectMerger<TestLocalEntity, TestRemoteEntity> buildLocalToRemoteMerger() {
            return localToRemoteMerger;
        }

        @Override
        protected ObjectMerger<TestRemoteEntity, TestLocalEntity> buildRemoteToLocalMerger() {
            return remoteToLocalMerger;
        }
    }

}
