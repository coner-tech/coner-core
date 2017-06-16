package org.coner.core.domain.service;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.payload.RunAddTimePayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.RunGateway;

public class RunEntityService extends AbstractEntityService<
        Run,
        RunAddPayload,
        RunGateway> {

    private final EventEntityService eventEntityService;

    @Inject
    public RunEntityService(RunGateway gateway, EventEntityService eventEntityService) {
        super(Run.class, gateway);
        this.eventEntityService = eventEntityService;
    }

    @Override
    public Run add(RunAddPayload addPayload) throws AddEntityException {
        Run lastInSequenceForEvent = gateway.findLastInSequenceForEvent(addPayload.getEvent());
        addPayload.setSequence(
                1 + (lastInSequenceForEvent != null ? lastInSequenceForEvent.getSequence() : 0)
        );
        return gateway.add(addPayload);
    }

    public Run getByEventIdAndRunId(String eventId, String runId)
            throws EntityMismatchException, EntityNotFoundException {
        Run run = gateway.findById(runId);
        if (!run.getEvent().getId().equals(eventId)) {
            throw new EntityMismatchException();
        }
        return run;
    }

    public List<Run> getAllWithEventId(String eventId) throws EntityNotFoundException {
        Event event = eventEntityService.getById(eventId);
        return gateway.getAllWith(event);
    }

    public Run addTimeToFirstRunInSequenceWithoutRawTime(RunAddTimePayload addTimePayload)
            throws EntityNotFoundException, AddEntityException {
        Run firstRunInSequenceWithoutTime = gateway.findFirstInSequenceWithoutTime(addTimePayload.getEvent());
        if (firstRunInSequenceWithoutTime == null) {
            RunAddPayload addPayload = new RunAddPayload();
            addPayload.setEvent(addTimePayload.getEvent());
            addPayload.setTimestamp(Instant.now());
            addPayload.setRawTime(addTimePayload.getRawTime());
            return add(addPayload);
        } else {
            firstRunInSequenceWithoutTime.setRawTime(addTimePayload.getRawTime());
            return gateway.save(firstRunInSequenceWithoutTime.getId(), firstRunInSequenceWithoutTime);
        }
    }
}
