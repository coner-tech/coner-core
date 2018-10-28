package org.coner.core.domain.interactor;

import java.math.BigDecimal;
import java.util.Map;

import javax.inject.Inject;

import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.value.HandicapTimeScoringMethod;

public class RunScoringInteractor {

    private final RawTimeScoringInteractor rawTimeScoringInteractor;
    private final Map<HandicapTimeScoringMethod, HandicapTimeScoringInteractor> handicapTimeScoringInteractors;

    public static final BigDecimal TIME_DID_NOT_FINISH = BigDecimal.valueOf(Long.MAX_VALUE, 3);

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

        if (!run.getDidNotFinish()) {
            rawTimeScoringInteractor.score(scoredRun);
            HandicapTimeScoringMethod handicapTimeScoringMethod = run.getEvent().getHandicapTimeScoringMethod();
            if (handicapTimeScoringInteractors.containsKey(handicapTimeScoringMethod)) {
                handicapTimeScoringInteractors.get(handicapTimeScoringMethod)
                        .score(scoredRun);
            } else {
                throw new UnsupportedOperationException(
                        "Unsupported HandicapTimeScoringMethod: " + handicapTimeScoringMethod
                );
            }
        } else {
            scoredRun.setRawTimeScored(TIME_DID_NOT_FINISH);
            scoredRun.setHandicapTimeScored(TIME_DID_NOT_FINISH);
        }

        return scoredRun;
    }

    boolean shouldScore(Run run) {
        if (run == null) return false;
        if (run.getRawTime() == null) return false;
        if (run.getRerun()) return false;
        if (!run.getCompetitive()) return false;
        if (run.getDisqualified()) return false;
        return true;
    }

}
