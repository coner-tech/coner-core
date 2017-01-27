package org.coner.api.response;

import java.util.List;

import org.coner.api.entity.RegistrationApiEntity;

public class GetEventRegistrationsResponse {

    private List<RegistrationApiEntity> registrations;

    public List<RegistrationApiEntity> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<RegistrationApiEntity> registrations) {
        this.registrations = registrations;
    }
}
