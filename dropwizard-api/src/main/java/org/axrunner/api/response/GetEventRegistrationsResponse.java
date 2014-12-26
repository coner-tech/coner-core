package org.axrunner.api.response;

import org.axrunner.api.entity.Registration;

import java.util.List;

/**
 *
 */
public class GetEventRegistrationsResponse {

    private List<Registration> registrations;

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
}
