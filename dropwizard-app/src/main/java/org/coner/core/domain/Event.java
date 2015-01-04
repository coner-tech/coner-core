package org.coner.core.domain;

import java.util.Date;

/**
 *
 */
public class Event extends DomainEntity {

    private String id;
    private String name;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public boolean hasDate() {
        return date != null;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
