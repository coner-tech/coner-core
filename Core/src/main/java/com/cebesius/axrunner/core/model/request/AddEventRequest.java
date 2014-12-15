package com.cebesius.axrunner.core.model.request;

/**
 *
 */
public class AddEventRequest {

    private String name;

    public AddEventRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
