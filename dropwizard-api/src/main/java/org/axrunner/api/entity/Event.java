package org.axrunner.api.entity;

import org.joda.time.DateTime;

/**
 *
 */
public class Event extends ApiEntity {

    private String id;
    private String name;
    private DateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
