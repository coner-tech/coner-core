package com.cebesius.axrunner.core.model.domain;

/**
 *
 */
public class Event {

    private final String id;
    private final String name;

    public Event(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
