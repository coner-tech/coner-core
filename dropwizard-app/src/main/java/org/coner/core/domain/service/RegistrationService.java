package org.coner.core.domain.service;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.core.exception.EventMismatchException;
import org.coner.core.gateway.RegistrationGateway;

public class RegistrationService extends AbstractDomainService<
        Registration,
        RegistrationAddPayload,
        RegistrationGateway> {

    @Inject
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
