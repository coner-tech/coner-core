package org.coner.api.response;

import org.coner.api.entity.RegistrationApiEntity;

import java.util.List;

public class GetEventRegistrationsResponse {

    private List<RegistrationApiEntity> registrations;

    public List<RegistrationApiEntity> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<RegistrationApiEntity> registrations) {
        this.registrations = registrations;
    }
}
