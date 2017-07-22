package org.coner.core.dagger;

import org.coner.core.domain.interactor.FlatConePenaltyHandicapTimeScoringInteractor;
import org.coner.core.domain.interactor.HandicapTimeScoringInteractor;
import org.coner.core.domain.interactor.TraditionalHandicapTimeScoringInteractor;
import org.coner.core.domain.value.HandicapTimeScoringMethod;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class RunScoringModule {

    @Provides
    @IntoMap
    @HandicapTimeScoringMethodKey(HandicapTimeScoringMethod.SCALED_CONE_PENALTY_TRADITIONAL)
    public HandicapTimeScoringInteractor scaled() {
        return new TraditionalHandicapTimeScoringInteractor();
    }

    @Provides
    @IntoMap
    @HandicapTimeScoringMethodKey(HandicapTimeScoringMethod.UNSCALED_CONE_PENALTY)
    public HandicapTimeScoringInteractor unscaled() {
        return new FlatConePenaltyHandicapTimeScoringInteractor();
    }
}
