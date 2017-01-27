package org.coner.core.api.response;

import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;

public class GetEventRegistrationsResponse {

    private List<RegistrationApiEntity> registrations;

    public List<RegistrationApiEntity> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<RegistrationApiEntity> registrations) {
        this.registrations = registrations;
    }
}
