package org.axrunner.core.gateway;

import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.domain.Event;
import org.axrunner.hibernate.dao.EventDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.*;

/**
 *
 */
public class EventGatewayTest {

    @Mock
    EventBoundary eventBoundary;
    @Mock
    EventDao eventDao;

    private EventGateway eventGateway;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        eventGateway = new EventGateway(eventBoundary, eventDao);
    }

    @Test
    public void whenGetAllItShouldFindAllAndConvertToDomainEntities() {
        List<org.axrunner.hibernate.entity.Event> hibernateEvents = new ArrayList<>();
        List<Event> expected = new ArrayList<>();
        when(eventDao.findAll()).thenReturn(hibernateEvents);
        when(eventBoundary.toDomainEntities(hibernateEvents)).thenReturn(expected);

        List<Event> actual = eventGateway.getAll();

        verify(eventDao).findAll();
        verify(eventBoundary).toDomainEntities(hibernateEvents);
        verifyNoMoreInteractions(eventDao);
        verifyNoMoreInteractions(eventBoundary);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void whenFindByIdItShouldFindAndConvertToDomainEntity() {
        String id = "test-id";
        org.axrunner.hibernate.entity.Event hibernateEvent = mock(org.axrunner.hibernate.entity.Event.class);
        Event expected = mock(Event.class);
        when(eventDao.findById(id)).thenReturn(hibernateEvent);
        when(eventBoundary.toDomainEntity(hibernateEvent)).thenReturn(expected);

        Event actual = eventGateway.findById(id);

        verify(eventDao).findById(id);
        verify(eventBoundary).toDomainEntity(hibernateEvent);
        verifyNoMoreInteractions(eventDao);
        verifyNoMoreInteractions(eventBoundary);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void whenFindByInvalidIdItShouldThrow() {
        String[] invalidIds = new String[]{null, ""};
        for (String invalidId : invalidIds) {
            try {
                eventGateway.findById(invalidId);
                failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
            } catch (Exception e) {
                assertThat(e).isInstanceOf(IllegalArgumentException.class);
                verifyZeroInteractions(eventDao);
                verifyZeroInteractions(eventBoundary);
            }
        }
    }

    @Test
    public void whenCreateItShouldConvertToHibernateAndCreateAndMergeHibernateIntoDomain() {
        Event event = mock(Event.class);
        org.axrunner.hibernate.entity.Event hibernateEvent = mock(org.axrunner.hibernate.entity.Event.class);
        when(eventBoundary.toHibernateEntity(event)).thenReturn(hibernateEvent);

        eventGateway.create(event);

        verify(eventBoundary).toHibernateEntity(event);
        verify(eventDao).create(hibernateEvent);
        verify(eventBoundary).merge(hibernateEvent, event);
        verifyNoMoreInteractions(eventBoundary);
        verifyNoMoreInteractions(eventDao);
    }

    @Test
    public void whenCreateInvalidEventItShouldThrow() {
        Event event = null;
        try {
            eventGateway.create(event);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(eventDao);
            verifyZeroInteractions(eventBoundary);
        }
    }

}
