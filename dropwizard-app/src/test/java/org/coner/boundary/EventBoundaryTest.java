package org.coner.boundary;

import org.coner.core.domain.Event;
import org.coner.util.ApiEntityUtils;
import org.coner.util.DomainUtils;
import org.coner.util.HibernateEntityUtils;
import org.coner.util.TestConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class EventBoundaryTest {

    private EventBoundary eventBoundary;

    private final String id = TestConstants.EVENT_ID;
    private final String name = TestConstants.EVENT_NAME;
    private final Date date = TestConstants.EVENT_DATE;

    @Before
    public void setup() {
        EventBoundary.setInstance(null);
        eventBoundary = new EventBoundary();
    }

    @After
    public void tearDown() {
        EventBoundary.setInstance(null);
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
        org.coner.api.entity.Event apiEvent = ApiEntityUtils.fullApiEvent();
        Event domainEvent = new Event();

        eventBoundary.merge(apiEvent, domainEvent);

        assertThat(domainEvent.getId()).isEqualTo(id);
        assertThat(domainEvent.getName()).isEqualTo(name);
        assertThat(domainEvent.hasDate()).isTrue();
        assertThat(domainEvent.getDate())
                .isEqualTo(date)
                .isNotSameAs(date);
    }

    @Test
    public void whenMergeApiWithoutDateIntoDomainItShouldNpe() {
        org.coner.api.entity.Event apiEvent = ApiEntityUtils.fullApiEvent();
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
        Event domainEvent = DomainUtils.fullDomainEvent();
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
        Event domainEvent = DomainUtils.fullDomainEvent();
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
        Event domainEvent = DomainUtils.fullDomainEvent();
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
        Event domainEvent = DomainUtils.fullDomainEvent();
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
        org.coner.hibernate.entity.Event hibernateEvent = HibernateEntityUtils.fullHibernateEvent();
        Event domainEvent = new Event();

        eventBoundary.merge(hibernateEvent, domainEvent);

        assertThat(domainEvent.getId()).isEqualTo(id);
        assertThat(domainEvent.getName()).isEqualToIgnoringCase(name);
        assertThat(domainEvent.getDate())
                .isEqualTo(date);
    }
}
