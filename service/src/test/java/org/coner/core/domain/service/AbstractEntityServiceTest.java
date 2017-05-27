package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.coner.core.domain.entity.DomainEntity;
import org.coner.core.domain.payload.DomainAddPayload;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.Gateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractEntityServiceTest {

    private static final String TEST_ID = "test-id";

    private TestEntityService service;
    @Mock
    private TestGateway gateway;
    @Mock
    private TestEntity domainEntity;
    @Mock
    private TestAddPayload addPayload;

    @Before
    public void setup() {
        service = new TestEntityService(gateway);
    }

    @Test
    public void whenGetByIdWithExistingIdItShouldReturnEntity() throws EntityNotFoundException {
        when(gateway.findById(TEST_ID)).thenReturn(domainEntity);

        TestEntity actual = service.getById(TEST_ID);

        verify(gateway).findById(TEST_ID);
        verifyNoMoreInteractions(gateway);
        assertThat(actual).isSameAs(domainEntity);
    }

    @Test
    public void whenGetByIdWithNotExistingIdItShouldThrowEntityNotFoundExceptionWithDescriptiveMessage() {
        when(gateway.findById(TEST_ID)).thenReturn(null);

        try {
            TestEntity actual = service.getById(TEST_ID);
            failBecauseExceptionWasNotThrown(EntityNotFoundException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(EntityNotFoundException.class);
            assertThat(e)
                    .hasMessageContaining(TestEntity.class.getSimpleName())
                    .hasMessageContaining("not found");
        }
    }

    private static class TestEntityService extends AbstractEntityService<
            TestEntity,
            TestAddPayload,
            TestGateway> {

        protected TestEntityService(TestGateway gateway) {
            super(TestEntity.class, gateway);
        }
    }

    private static class TestGateway implements Gateway<TestEntity, TestAddPayload> {

        @Override
        public TestEntity add(TestAddPayload addPayload) {
            return null;
        }

        @Override
        public List<TestEntity> getAll() {
            return null;
        }

        @Override
        public TestEntity findById(String id) {
            return null;
        }

        @Override
        public TestEntity save(String id, TestEntity entity) {
            return null;
        }
    }

    private static class TestEntity extends DomainEntity {

    }

    private static class TestAddPayload extends DomainAddPayload {

    }
}

