package org.coner.core.domain.service;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.gateway.RegistrationGateway;

public class RegistrationEntityService extends AbstractEntityService<
        Registration,
        RegistrationAddPayload,
        RegistrationGateway> {

    @Inject
    public RegistrationEntityService(RegistrationGateway gateway) {
        super(Registration.class, gateway);
    }

    public List<Registration> getAllWith(Event event) {
        return gateway.getAllWith(event);
    }
}
