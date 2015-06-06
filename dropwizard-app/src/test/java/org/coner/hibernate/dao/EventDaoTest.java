package org.coner.hibernate.dao;

import org.coner.hibernate.entity.Event;

import java.time.ZonedDateTime;
import java.util.*;
import org.hibernate.Query;
import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;

public class EventDaoTest extends AbstractDaoTest {

    private final String name = "EventDao test event";
    private final Date date = Date.from(ZonedDateTime.parse("2014-12-26T22:12:00-05:00").toInstant());
    private EventDao eventDao;

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
