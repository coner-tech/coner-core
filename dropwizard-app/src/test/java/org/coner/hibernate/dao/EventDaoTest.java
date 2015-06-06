package org.coner.hibernate.dao;

import org.coner.hibernate.entity.EventHibernateEntity;

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

        Query delete = getSession().createQuery("delete from EventHibernateEntity");
        delete.executeUpdate();

        getSession().getTransaction().commit();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void whenFindAllItShouldReturnEmpty() {
        getSession().beginTransaction();

        List<EventHibernateEntity> actual = eventDao.findAll();
        assertThat(actual)
                .isNotNull()
                .isEmpty();

        getSession().getTransaction().commit();
    }

    @Test
    public void whenCreateItShouldCreateEvent() {
        EventHibernateEntity newEvent = buildNewEvent();

        getSession().beginTransaction();

        eventDao.create(newEvent);

        getSession().getTransaction().commit();

        assertThat(newEvent.getId())
                .isNotEmpty();
    }

    @Test
    public void whenCreatedItShouldFindById() {
        EventHibernateEntity newEvent = buildNewEvent();
        getSession().beginTransaction();
        eventDao.create(newEvent);
        getSession().getTransaction().commit();

        getSession().beginTransaction();

        EventHibernateEntity actual = eventDao.findById(newEvent.getId());

        getSession().getTransaction().commit();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(newEvent);
    }

    @Test
    public void whenCreateItShouldBeInFindAll() {
        EventHibernateEntity newEvent = buildNewEvent();
        getSession().beginTransaction();
        eventDao.create(newEvent);
        getSession().getTransaction().commit();

        getSession().beginTransaction();

        List<EventHibernateEntity> events = eventDao.findAll();

        getSession().getTransaction().commit();

        assertThat(events)
                .isNotNull()
                .isNotEmpty()
                .containsOnly(newEvent);
    }

    private EventHibernateEntity buildNewEvent() {
        EventHibernateEntity event = new EventHibernateEntity();
        event.setName(name);
        event.setDate(date);
        assertThat(event.getId()).isNull();
        return event;
    }
}
