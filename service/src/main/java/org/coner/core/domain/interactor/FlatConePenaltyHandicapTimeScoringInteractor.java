package org.coner.core.domain.interactor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;

/**
 * Calculates a handicap time using a flat cone penalty and assigns it to a ScoredRun.
 * The formula is:
 * handicapTimeScored = (scratch time * handicap group factor * competition group factor) + (secondsPerCone * cones)
 */
public class FlatConePenaltyHandicapTimeScoringInteractor implements HandicapTimeScoringInteractor {

    private final long secondsPerCone;

    @Inject
    public FlatConePenaltyHandicapTimeScoringInteractor(long secondsPerCone) {
        this.secondsPerCone = secondsPerCone;
    }

    @Override
    public void score(ScoredRun scoredRun) {
        Run run = scoredRun.getRun();
        BigDecimal scratchTime = run.getRawTime();
        BigDecimal handicapGroupFactor = run.getRegistration().getHandicapGroup().getFactor();
        BigDecimal competitionGroupFactor = run.getRegistration().getCompetitionGroup().getFactor();
        BigDecimal handicapTimeScratch = scratchTime.multiply(handicapGroupFactor)
                .multiply(competitionGroupFactor)
                .setScale(3, RoundingMode.HALF_UP);
        BigDecimal conePenaltySeconds = BigDecimal.valueOf(secondsPerCone * run.getCones());
        BigDecimal handicapTimeScored = handicapTimeScratch.add(conePenaltySeconds);
        scoredRun.setHandicapTimeScored(handicapTimeScored);
    }
}
