package com.cebesius.axrunner.core.model.response;

import com.cebesius.axrunner.core.model.domain.Registration;

/**
 *
 */
public class AddRegistrationResponse {

    private final Registration registration;

    public AddRegistrationResponse(Registration registration) {
        this.registration = registration;
    }

    public Registration getRegistration() {
        return registration;
    }
}
