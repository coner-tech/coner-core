package org.coner.core.domain.service;

import org.coner.core.domain.entity.*;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.exception.*;
import org.coner.core.gateway.RegistrationGateway;

import java.util.List;

public class RegistrationService extends AbstractDomainService<
        Registration,
        RegistrationAddPayload,
        RegistrationGateway> {

    public RegistrationService(RegistrationGateway gateway) {
        super(Registration.class, gateway);
    }

    public Registration getByIdWithEvent(String id, Event event)
            throws EventMismatchException, EntityNotFoundException {
        Registration registration = getById(id);
        if (!registration.getEvent().getId().equals(event.getId())) {
            throw new EventMismatchException();
        }
        return registration;
    }

    public List<Registration> getAllWith(Event event) {
        return gateway.getAllWith(event);
    }

    @Override
    public Registration add(RegistrationAddPayload addPayload) {
        return super.add(addPayload);
    }
}
