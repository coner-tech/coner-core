package org.coner.boundary;

import org.coner.core.domain.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class EventBoundaryTest {

    private EventBoundary eventBoundary;

    private final String id = "test-id";
    private final String name = "test-name";
    private final Date date = Date.from(ZonedDateTime.parse("2014-12-26T19:44:00-05:00").toInstant());

    @Before
    public void setup() {
        EventBoundary.instance = null;
        eventBoundary = new EventBoundary();
    }

    @After
    public void tearDown() {
        EventBoundary.instance = null;
    }

    @Test
    public void whenGetInstanceItShouldInstantiateAndReturnSingleton() {
        EventBoundary one = EventBoundary.getInstance();
        EventBoundary two = EventBoundary.getInstance();

        assertThat(one)
                .isNotNull()
                .isSameAs(two);
    }

    @Test
    public void whenMergeApiIntoDomainItShouldMerge() {
        org.coner.api.entity.Event apiEvent = fullApiEvent();
        Event domainEvent = new Event();

        eventBoundary.merge(apiEvent, domainEvent);

        assertThat(domainEvent.getEventId()).isEqualTo(id);
        assertThat(domainEvent.getName()).isEqualTo(name);
        assertThat(domainEvent.getDate())
                .isEqualTo(date)
                .isNotSameAs(date);
    }

    @Test
    public void whenMergeApiWithoutDateIntoDomainItShouldNpe() {
        org.coner.api.entity.Event apiEvent = fullApiEvent();
        apiEvent.setDate(null);
        Event domainEvent = new Event();

        try {
            eventBoundary.merge(apiEvent, domainEvent);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void whenMergeDomainIntoApiItShouldMerge() {
        Event domainEvent = fullDomainEvent();
        org.coner.api.entity.Event apiEvent = new org.coner.api.entity.Event();

        eventBoundary.merge(domainEvent, apiEvent);

        assertThat(apiEvent.getId()).isEqualTo(id);
        assertThat(apiEvent.getName()).isEqualTo(name);
        assertThat(apiEvent.getDate())
                .isEqualTo(date)
                .isNotSameAs(date);
    }

    @Test
    public void whenMergeDomainWithoutDateIntoDomainItShouldNpe() {
        Event domainEvent = fullDomainEvent();
        domainEvent.setDate(null);
        org.coner.api.entity.Event apiEvent = new org.coner.api.entity.Event();

        try {
            eventBoundary.merge(domainEvent, apiEvent);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void whenMergeDomainIntoHibernateItShouldMerge() {
        Event domainEvent = fullDomainEvent();
        org.coner.hibernate.entity.Event hibernateEvent = new org.coner.hibernate.entity.Event();

        eventBoundary.merge(domainEvent, hibernateEvent);

        assertThat(hibernateEvent.getId()).isEqualTo(id);
        assertThat(hibernateEvent.getName()).isEqualTo(name);
        assertThat(hibernateEvent.getDate())
                .isEqualTo(date)
                .isNotSameAs(date);
    }

    @Test
    public void whenMergeDomainWithoutDateIntoHibernateItShouldNpe() {
        Event domainEvent = fullDomainEvent();
        domainEvent.setDate(null);
        org.coner.hibernate.entity.Event hibernateEvent = new org.coner.hibernate.entity.Event();

        try {
            eventBoundary.merge(domainEvent, hibernateEvent);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void whenMergeHibernateIntoDomainItShouldMerge() {
        org.coner.hibernate.entity.Event hibernateEvent = fullHibernateEvent();
        Event domainEvent = new Event();

        eventBoundary.merge(hibernateEvent, domainEvent);

        assertThat(domainEvent.getEventId()).isEqualTo(id);
        assertThat(domainEvent.getName()).isEqualToIgnoringCase(name);
        assertThat(domainEvent.getDate())
                .isEqualTo(date)
                .isNotSameAs(date);
    }

    @Test
    public void whenMergeHibernateWithoutDateIntoDomainItShouldNpe() {
        org.coner.hibernate.entity.Event hibernateEvent = fullHibernateEvent();
        hibernateEvent.setDate(null);
        Event domainEvent = new Event();

        try {
            eventBoundary.merge(hibernateEvent, domainEvent);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    private org.coner.api.entity.Event fullApiEvent() {
        org.coner.api.entity.Event apiEvent = new org.coner.api.entity.Event();
        apiEvent.setId(id);
        apiEvent.setName(name);
        apiEvent.setDate(date);
        return apiEvent;
    }

    private Event fullDomainEvent() {
        Event domainEvent = new Event();
        domainEvent.setEventId(id);
        domainEvent.setName(name);
        domainEvent.setDate(date);
        return domainEvent;
    }

    private org.coner.hibernate.entity.Event fullHibernateEvent() {
        org.coner.hibernate.entity.Event hibernateEvent = new org.coner.hibernate.entity.Event();
        hibernateEvent.setId(id);
        hibernateEvent.setName(name);
        hibernateEvent.setDate(date);
        return hibernateEvent;
    }

}
