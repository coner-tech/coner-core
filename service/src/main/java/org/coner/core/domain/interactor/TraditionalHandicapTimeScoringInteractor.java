package org.coner.core.domain.interactor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;

import com.google.common.base.Preconditions;

/**
 * Calculates a handicap time using a handicap-influenced cone penalty and assigns it to a ScoredRun.
 * The formula is:
 * handicapTimeScored = scored raw time * handicap group factor * competition group factor
 *
 * This is the method traditionally used by SCCA and many regional and local clubs. It has a bias towards
 * softer-handicapped drivers, because the cone penalty (already accounted for in scored raw time) is scaled in
 * conjunction with the handicap.
 */
public class TraditionalHandicapTimeScoringInteractor implements HandicapTimeScoringInteractor {

    @Override
    public void score(ScoredRun scoredRun) {
        BigDecimal rawTimeScored = scoredRun.getRawTimeScored();
        Preconditions.checkNotNull(rawTimeScored, "depends on raw time scored");
        Run run = scoredRun.getRun();
        BigDecimal handicapGroupFactor = run.getRegistration().getHandicapGroup().getFactor();
        BigDecimal competitionGroupFactor = run.getRegistration().getCompetitionGroup().getFactor();
        BigDecimal handicapTimeScored = rawTimeScored.multiply(handicapGroupFactor)
                .multiply(competitionGroupFactor)
                .setScale(3, RoundingMode.HALF_UP);
        scoredRun.setHandicapTimeScored(handicapTimeScored);
    }
}
