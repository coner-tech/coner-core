package org.coner.core.domain.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.coner.core.dagger.DaggerEventScoringComponent;
import org.coner.core.dagger.EventScoringComponent;
import org.coner.core.dagger.EventScoringModule;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.interactor.RunScoringInteractor;
import org.coner.core.domain.payload.GetRegistrationResultsPayload;

public class ResultsService {

    private final EventRegistrationService eventRegistrationService;
    private final RunEntityService runEntityService;

    @Inject
    public ResultsService(EventRegistrationService eventRegistrationService, RunEntityService runEntityService) {
        this.eventRegistrationService = eventRegistrationService;
        this.runEntityService = runEntityService;
    }

    public GetRegistrationResultsPayload getResultsFor(Registration registration) {
        GetRegistrationResultsPayload payload = new GetRegistrationResultsPayload();
        payload.setRegistration(registration);

        EventScoringComponent eventScoringComponent = DaggerEventScoringComponent.builder()
                .eventScoringModule(new EventScoringModule(registration.getEvent()))
                .build();
        RunScoringInteractor interactor = eventScoringComponent.runScoringInteractor();

        List<Run> runs = runEntityService.getAllWithRegistration(registration);
        List<ScoredRun> scoredRuns = new ArrayList<>(runs.size());
        for (Run run : runs) {
            ScoredRun scoredRun = interactor.score(run);
            if (scoredRun != null) {
                scoredRuns.add(scoredRun);
            }
        }
        payload.setScoredRuns(scoredRuns);

        payload.setScore(scoredRuns.stream()
                .min(Comparator.comparing(ScoredRun::getRawTimeScored))
                .orElse(null));

        return payload;
    }
}
