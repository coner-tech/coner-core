package org.coner.core.domain.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.interactor.RunScoringInteractor;
import org.coner.core.domain.payload.GetRegistrationResultsPayload;

public class ResultsService {

    private final RunEntityService runEntityService;
    private final RunScoringInteractor runScoringInteractor;

    @Inject
    public ResultsService(
            RunEntityService runEntityService,
            RunScoringInteractor runScoringInteractor
    ) {
        this.runEntityService = runEntityService;
        this.runScoringInteractor = runScoringInteractor;
    }

    public GetRegistrationResultsPayload getResultsFor(Registration registration) {
        GetRegistrationResultsPayload payload = new GetRegistrationResultsPayload();
        payload.setRegistration(registration);

        List<Run> runs = runEntityService.getAllWithRegistration(registration);
        int maxRunsPerRegistration = registration.getEvent().getMaxRunsPerRegistration();
        List<ScoredRun> scoredRuns = new ArrayList<>(runs.size());
        for (Run run : runs) {
            if (scoredRuns.size() >= maxRunsPerRegistration) {
                break;
            }
            ScoredRun scoredRun = runScoringInteractor.score(run);
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
