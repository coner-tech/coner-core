package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.coner.core.domain.entity.DomainEntity;
import org.coner.core.domain.payload.DomainAddPayload;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.core.gateway.Gateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractDomainServiceTest {

    private static final String TEST_ID = "test-id";

    private TestDomainService service;
    @Mock
    private TestGateway gateway;
    @Mock
    private TestEntity domainEntity;
    @Mock
    private TestAddPayload addPayload;

    @Before
    public void setup() {
        service = new TestDomainService(gateway);
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

    private static class TestDomainService extends AbstractDomainService<
            TestEntity,
            TestAddPayload,
            TestGateway> {

        protected TestDomainService(TestGateway gateway) {
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
    }

    private static class TestEntity extends DomainEntity {

    }

    private static class TestAddPayload extends DomainAddPayload {

    }
}

