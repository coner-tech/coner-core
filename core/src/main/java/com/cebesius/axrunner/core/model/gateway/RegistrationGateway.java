package com.cebesius.axrunner.core.model.gateway;

import com.cebesius.axrunner.core.model.domain.Event;
import com.cebesius.axrunner.core.model.domain.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class RegistrationGateway {
    public List<Registration> getAllWithEvent(Event event) {
        int count = (int) Math.round(Math.random() * 100);
        List<Registration> registrations = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            registrations.add(buildFakeRegistrationWithEvent(event));
        }
        return registrations;
    }

    public Registration create(Event event, String firstName, String lastName) {
        return new Registration(
                UUID.randomUUID().toString(),
                event.getId(),
                firstName,
                lastName
        );
    }

    private Registration buildFakeRegistrationWithEvent(Event event) {
        return new Registration(UUID.randomUUID().toString(),
                event.getId(),
                "Random",
                "Driver"
        );
    }


}
