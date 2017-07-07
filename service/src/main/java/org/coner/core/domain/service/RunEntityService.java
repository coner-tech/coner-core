package org.coner.core.domain.service;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.payload.RunAddTimePayload;
import org.coner.core.domain.payload.RunTimeAddedPayload;
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

    public RunTimeAddedPayload addTimeToFirstRunInSequenceWithoutRawTime(RunAddTimePayload addTimePayload)
            throws EntityNotFoundException, AddEntityException {
        RunTimeAddedPayload runTimeAddedPayload = new RunTimeAddedPayload();
        Run firstRunInSequenceWithoutTime = gateway.findFirstInSequenceWithoutTime(addTimePayload.getEvent());
        if (firstRunInSequenceWithoutTime != null) {
            firstRunInSequenceWithoutTime.setRawTime(addTimePayload.getRawTime());
            Run runWithRawTimeAssigned = gateway.save(
                    firstRunInSequenceWithoutTime.getId(),
                    firstRunInSequenceWithoutTime
            );
            runTimeAddedPayload.setRun(runWithRawTimeAssigned);
            runTimeAddedPayload.setOutcome(RunTimeAddedPayload.Outcome.RUN_RAWTIME_ASSIGNED_TO_EXISTING);
        } else {
            // Consider error reporting for this scenario.
            // There has possibly been a false finish trip, or perhaps a car managed to stage and launch without
            // being noticed by Timing workers. Recommend to hold start while resolving situation.
            RunAddPayload addPayload = new RunAddPayload();
            addPayload.setEvent(addTimePayload.getEvent());
            addPayload.setTimestamp(Instant.now());
            addPayload.setRawTime(addTimePayload.getRawTime());
            Run addedRun = add(addPayload);
            runTimeAddedPayload.setRun(addedRun);
            runTimeAddedPayload.setOutcome(RunTimeAddedPayload.Outcome.RUN_ADDED_WITH_RAWTIME);
        }
        return runTimeAddedPayload;
    }

    public List<Run> getAllWithRegistration(Registration registration) {
        return gateway.getAllWith(registration);
    }
}
