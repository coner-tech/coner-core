package org.coner.core.domain.interactor;

import javax.inject.Inject;

import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;

public class RunScoringInteractor {

    private final RawTimeScoringInteractor rawTimeScoringInteractor;
    private final HandicapTimeScoringInteractor handicapTimeScoringInteractor;

    @Inject
    public RunScoringInteractor(
            RawTimeScoringInteractor rawTimeScoringInteractor,
            HandicapTimeScoringInteractor handicapTimeScoringInteractor
    ) {
        this.rawTimeScoringInteractor = rawTimeScoringInteractor;
        this.handicapTimeScoringInteractor = handicapTimeScoringInteractor;
    }

    public ScoredRun score(Run run) {
        if (!shouldScore(run)) {
            return null;
        }
        ScoredRun scoredRun = new ScoredRun();
        scoredRun.setRun(run);

        rawTimeScoringInteractor.score(scoredRun);
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
