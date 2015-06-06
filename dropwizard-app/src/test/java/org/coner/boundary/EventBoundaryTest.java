package org.coner.boundary;

import org.coner.api.entity.EventApiEntity;
import org.coner.core.domain.Event;
import org.coner.hibernate.entity.EventHibernateEntity;
import org.coner.util.*;

import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;

public class EventBoundaryTest {

    private final String id = TestConstants.EVENT_ID;
    private final String name = TestConstants.EVENT_NAME;
    private final Date date = TestConstants.EVENT_DATE;
    private EventBoundary eventBoundary;

    @Before
    public void setup() {
        eventBoundary = new EventBoundary();
    }

    @Test
    public void whenMergeApiIntoDomainItShouldMerge() {
        EventApiEntity apiEvent = ApiEntityTestUtils.fullApiEvent();
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
        EventApiEntity apiEvent = ApiEntityTestUtils.fullApiEvent();
        apiEvent.setDate(null);
        Event domainEvent = new Event();

        eventBoundary.merge(apiEvent, domainEvent);

        assertThat(domainEvent.getDate()).isNull();
    }

    @Test
    public void whenMergeDomainIntoApiItShouldMerge() {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        EventApiEntity apiEvent = new EventApiEntity();

        eventBoundary.merge(domainEvent, apiEvent);

        assertThat(apiEvent.getId()).isEqualTo(id);
        assertThat(apiEvent.getName()).isEqualTo(name);
        assertThat(apiEvent.getDate())
                .isEqualTo(date);
    }

    @Test
    public void whenMergeDomainWithoutDateIntoDomainItShouldBeWithoutDate() {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        domainEvent.setDate(null);
        EventApiEntity apiEvent = new EventApiEntity();

        eventBoundary.merge(domainEvent, apiEvent);

        Assertions.assertThat(apiEvent.getDate()).isNull();
    }

    @Test
    public void whenMergeDomainIntoHibernateItShouldMerge() {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        EventHibernateEntity hibernateEvent = new EventHibernateEntity();

        eventBoundary.merge(domainEvent, hibernateEvent);

        assertThat(hibernateEvent.getId()).isEqualTo(id);
        assertThat(hibernateEvent.getName()).isEqualTo(name);
        assertThat(hibernateEvent.getDate())
                .isEqualTo(date);
    }

    @Test
    public void whenMergeDomainWithoutDateIntoHibernateItShouldBeWithoutDate() {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        domainEvent.setDate(null);
        EventHibernateEntity hibernateEvent = new EventHibernateEntity();

        eventBoundary.merge(domainEvent, hibernateEvent);

        Assertions.assertThat(hibernateEvent.getDate())
                .isNull();
    }

    @Test
    public void whenMergeHibernateIntoDomainItShouldMerge() {
        EventHibernateEntity hibernateEvent = HibernateEntityUtils.fullHibernateEvent();
        Event domainEvent = new Event();

        eventBoundary.merge(hibernateEvent, domainEvent);

        assertThat(domainEvent.getId()).isEqualTo(id);
        assertThat(domainEvent.getName()).isEqualToIgnoringCase(name);
        assertThat(domainEvent.getDate())
                .isEqualTo(date);
    }
}
