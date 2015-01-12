package org.coner.api.response;

import org.coner.api.entity.Registration;

import java.util.List;

/**
 * API response object containing a list of Registration entities for an Event.
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
