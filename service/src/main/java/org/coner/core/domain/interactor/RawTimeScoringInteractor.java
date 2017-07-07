package org.coner.core.domain.interactor;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.coner.core.domain.entity.ScoredRun;

/**
 * Calculates a raw time and assigns it to a ScoredRun.
 * The formula is: rawTime = scratch time + (secondsPerCone * cones)
 */
public class RawTimeScoringInteractor {

    private final long secondsPerCone;

    @Inject
    public RawTimeScoringInteractor(long secondsPerCone) {
        this.secondsPerCone = secondsPerCone;
    }

    public void score(ScoredRun scoredRun) {
        BigDecimal conePenaltySeconds = BigDecimal.valueOf(secondsPerCone * scoredRun.getRun().getCones());
        BigDecimal rawTimeScored = scoredRun.getRun().getRawTime().add(conePenaltySeconds);
        scoredRun.setRawTimeScored(rawTimeScored);
    }
}
