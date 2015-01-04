package org.coner.util;

import org.coner.hibernate.entity.Event;

import java.util.Date;

/**
 *
 */
public final class HibernateEntityUtils {

    private HibernateEntityUtils() {
    }

    public static Event fullHibernateEvent() {
        return fullHibernateEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static Event fullHibernateEvent(String eventId, String eventName, Date eventDate) {
        Event hibernateEvent = new Event();
        hibernateEvent.setId(eventId);
        hibernateEvent.setName(eventName);
        hibernateEvent.setDate(eventDate);
        return hibernateEvent;
    }
}
