package org.coner.core.domain.interactor;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.coner.core.domain.entity.ScoredRun;

/**
 * Calculates a raw time and assigns it to a ScoredRun.
 * The formula is: rawTime = scratch time + (secondsPerCone * cones)
 */
public class RawTimeScoringInteractor {

    @Inject
    public RawTimeScoringInteractor() {
        // no-op
    }

    public void score(ScoredRun scoredRun) {
        BigDecimal eventConePenaltySeconds = scoredRun.getRun().getEvent().getConePenaltySeconds();
        BigDecimal runCones = BigDecimal.valueOf(scoredRun.getRun().getCones());
        BigDecimal conePenaltySeconds = eventConePenaltySeconds.multiply(runCones);
        BigDecimal rawTimeScored = scoredRun.getRun().getRawTime().add(conePenaltySeconds);
        scoredRun.setRawTimeScored(rawTimeScored);
    }
}
