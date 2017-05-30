package org.coner.core.domain.service;

import javax.inject.Inject;

import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.gateway.RunGateway;

public class RunEntityService extends AbstractEntityService<
        Run,
        RunAddPayload,
        RunGateway> {

    @Inject
    public RunEntityService(RunGateway gateway) {
        super(Run.class, gateway);
    }

    @Override
    public Run add(RunAddPayload addPayload) throws AddEntityException {
        Run lastInSequenceForEvent = gateway.findLastInSequenceForEvent(addPayload.getEvent());
        addPayload.setSequence(
                1 + (lastInSequenceForEvent != null ? lastInSequenceForEvent.getSequence() : 0)
        );
        return gateway.add(addPayload);
    }
}
