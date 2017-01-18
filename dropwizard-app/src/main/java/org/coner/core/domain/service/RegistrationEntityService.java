package org.coner.core.domain.service;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.RegistrationGateway;

public class RegistrationEntityService extends AbstractEntityService<
        Registration,
        RegistrationAddPayload,
        RegistrationGateway> {

    private final EventEntityService eventEntityService;

    @Inject
    public RegistrationEntityService(RegistrationGateway gateway, EventEntityService eventEntityService) {
        super(Registration.class, gateway);
        this.eventEntityService = eventEntityService;
    }

    public Registration getByEventIdAndRegistrationId(String eventId, String registrationId)
            throws EntityMismatchException, EntityNotFoundException {
        Event event = eventEntityService.getById(eventId);
        Registration registration = getById(registrationId);
        if (!registration.getEvent().getId().equals(event.getId())) {
            throw new EntityMismatchException();
        }
        return registration;
    }

    public List<Registration> getAllWithEventId(String eventId) throws EntityNotFoundException {
        Event event = eventEntityService.getById(eventId);
        return gateway.getAllWith(event);
    }

    @Override
    public Registration add(RegistrationAddPayload addPayload) throws AddEntityException {
        try {
            addPayload.event = eventEntityService.getById(addPayload.eventId);
        } catch (EntityNotFoundException e) {
            throw new AddEntityException(e);
        }
        return super.add(addPayload);
    }
}
