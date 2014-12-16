package com.cebesius.axrunner.core.model.response;

import com.cebesius.axrunner.core.model.domain.Registration;

import java.util.List;

/**
 *
 */
public class GetRegistrationsResponse {
    private final List<Registration> registrations;

    public GetRegistrationsResponse(List<Registration> registrations) {
        this.registrations = registrations;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }
}
