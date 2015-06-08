package org.coner.core.domain.service;

import org.coner.core.domain.entity.*;
import org.coner.core.exception.EventMismatchException;
import org.coner.core.gateway.RegistrationGateway;

import java.util.List;

public class RegistrationService extends AbstractDomainService<Registration, RegistrationGateway> {
    public RegistrationService(RegistrationGateway gateway) {
        super(gateway);
    }

    public Registration getByIdWithEvent(String id, Event event) throws EventMismatchException {
        Registration registration = getById(id);
        if (!registration.getEvent().getId().equals(event.getId())) {
            throw new EventMismatchException();
        }
        return registration;
    }

    public List<Registration> getAllWith(Event event) {
        return getGateway().getAllWith(event);
    }
}
