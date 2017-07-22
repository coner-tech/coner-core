package org.coner.core.domain.interactor;

import java.util.Map;

import javax.inject.Inject;

import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.value.HandicapTimeScoringMethod;

public class RunScoringInteractor {

    private final RawTimeScoringInteractor rawTimeScoringInteractor;
    private final Map<HandicapTimeScoringMethod, HandicapTimeScoringInteractor> handicapTimeScoringInteractors;

    @Inject
    public RunScoringInteractor(
            RawTimeScoringInteractor rawTimeScoringInteractor,
            Map<HandicapTimeScoringMethod, HandicapTimeScoringInteractor> handicapTimeScoringInteractors
    ) {
        this.rawTimeScoringInteractor = rawTimeScoringInteractor;
        this.handicapTimeScoringInteractors = handicapTimeScoringInteractors;
    }

    public ScoredRun score(Run run) {
        if (!shouldScore(run)) {
            return null;
        }
        ScoredRun scoredRun = new ScoredRun();
        scoredRun.setRun(run);

        rawTimeScoringInteractor.score(scoredRun);
        HandicapTimeScoringInteractor handicapTimeScoringInteractor;
        HandicapTimeScoringMethod handicapTimeScoringMethod = run.getEvent().getHandicapTimeScoringMethod();
        if (handicapTimeScoringInteractors.containsKey(handicapTimeScoringMethod)) {
            handicapTimeScoringInteractor = handicapTimeScoringInteractors.get(handicapTimeScoringMethod);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported HandicapTimeScoringMethod: " + handicapTimeScoringMethod
            );
        }
        handicapTimeScoringInteractor.score(scoredRun);

        return scoredRun;
    }

    boolean shouldScore(Run run) {
        if (run == null) return false;
        if (run.getRawTime() == null) return false;
        if (run.isRerun()) return false;
        if (!run.isCompetitive()) return false;
        return true;
    }

}
