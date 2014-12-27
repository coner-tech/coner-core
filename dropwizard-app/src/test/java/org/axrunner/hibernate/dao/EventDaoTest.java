package org.axrunner.hibernate.dao;

import org.axrunner.hibernate.entity.Event;
import org.hibernate.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class EventDaoTest extends AbstractDaoTest {

    private EventDao eventDao;

    private final String name = "EventDao test event";
    private final Date date = Date.from(ZonedDateTime.parse("2014-12-26T22:12:00-05:00").toInstant());

    @Before
    public void setup() {
        eventDao = new EventDao(getSessionFactory());

        getSession().beginTransaction();

        Query delete = getSession().createQuery("delete from Event");
        delete.executeUpdate();

        getSession().getTransaction().commit();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void whenFindAllItShouldReturnEmpty() {
        getSession().beginTransaction();

        List<Event> actual = eventDao.findAll();
        assertThat(actual)
                .isNotNull()
                .isEmpty();

        getSession().getTransaction().commit();
    }

    @Test
    public void whenCreateItShouldCreateEvent() {
        Event newEvent = buildNewEvent();

        getSession().beginTransaction();

        eventDao.create(newEvent);

        getSession().getTransaction().commit();

        assertThat(newEvent.getId())
                .isNotEmpty();
    }

    @Test
    public void whenCreatedItShouldFindById() {
        Event newEvent = buildNewEvent();
        getSession().beginTransaction();
        eventDao.create(newEvent);
        getSession().getTransaction().commit();

        getSession().beginTransaction();

        Event actual = eventDao.findById(newEvent.getId());

        getSession().getTransaction().commit();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(newEvent);
    }

    @Test
    public void whenCreateItShouldBeInFindAll() {
        Event newEvent = buildNewEvent();
        getSession().beginTransaction();
        eventDao.create(newEvent);
        getSession().getTransaction().commit();

        getSession().beginTransaction();

        List<Event> events = eventDao.findAll();

        getSession().getTransaction().commit();

        assertThat(events)
                .isNotNull()
                .isNotEmpty()
                .containsOnly(newEvent);
    }

    private Event buildNewEvent() {
        Event event = new Event();
        event.setName(name);
        event.setDate(date);
        assertThat(event.getId()).isNull();
        return event;
    }
}
