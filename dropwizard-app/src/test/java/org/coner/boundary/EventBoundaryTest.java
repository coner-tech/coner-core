package org.coner.boundary;

import org.assertj.core.api.Assertions;
import org.coner.core.domain.Event;
import org.coner.util.ApiEntityUtils;
import org.coner.util.DomainUtils;
import org.coner.util.HibernateEntityUtils;
import org.coner.util.TestConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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
        eventBoundary = new EventBoundary();
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
                .isEqualTo(date);
    }

    @Test
    public void whenMergeApiWithoutDateIntoDomainItShouldBeWithoutDate() {
        org.coner.api.entity.Event apiEvent = ApiEntityUtils.fullApiEvent();
        apiEvent.setDate(null);
        Event domainEvent = new Event();

        eventBoundary.merge(apiEvent, domainEvent);

        assertThat(domainEvent.getDate()).isNull();
    }

    @Test
    public void whenMergeDomainIntoApiItShouldMerge() {
        Event domainEvent = DomainUtils.fullDomainEvent();
        org.coner.api.entity.Event apiEvent = new org.coner.api.entity.Event();

        eventBoundary.merge(domainEvent, apiEvent);

        assertThat(apiEvent.getId()).isEqualTo(id);
        assertThat(apiEvent.getName()).isEqualTo(name);
        assertThat(apiEvent.getDate())
                .isEqualTo(date);
    }

    @Test
    public void whenMergeDomainWithoutDateIntoDomainItShouldBeWithoutDate() {
        Event domainEvent = DomainUtils.fullDomainEvent();
        domainEvent.setDate(null);
        org.coner.api.entity.Event apiEvent = new org.coner.api.entity.Event();

        eventBoundary.merge(domainEvent, apiEvent);

        Assertions.assertThat(apiEvent.getDate()).isNull();
    }

    @Test
    public void whenMergeDomainIntoHibernateItShouldMerge() {
        Event domainEvent = DomainUtils.fullDomainEvent();
        org.coner.hibernate.entity.Event hibernateEvent = new org.coner.hibernate.entity.Event();

        eventBoundary.merge(domainEvent, hibernateEvent);

        assertThat(hibernateEvent.getId()).isEqualTo(id);
        assertThat(hibernateEvent.getName()).isEqualTo(name);
        assertThat(hibernateEvent.getDate())
                .isEqualTo(date);
    }

    @Test
    public void whenMergeDomainWithoutDateIntoHibernateItShouldBeWithoutDate() {
        Event domainEvent = DomainUtils.fullDomainEvent();
        domainEvent.setDate(null);
        org.coner.hibernate.entity.Event hibernateEvent = new org.coner.hibernate.entity.Event();

        eventBoundary.merge(domainEvent, hibernateEvent);

        Assertions.assertThat(hibernateEvent.getDate())
                .isNull();
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
