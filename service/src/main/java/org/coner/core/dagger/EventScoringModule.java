package org.coner.core.dagger;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.interactor.FlatConePenaltyHandicapTimeScoringInteractor;
import org.coner.core.domain.interactor.HandicapTimeScoringInteractor;
import org.coner.core.domain.interactor.RawTimeScoringInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class EventScoringModule {

    private final Event event;

    public EventScoringModule(Event event) {
        this.event = event;
    }

    @Provides
    public RawTimeScoringInteractor provideRawTimeScoringInteractor() {
        // TODO: obtain cone penalty from event
        return new RawTimeScoringInteractor(2L);
    }

    @Provides
    public HandicapTimeScoringInteractor provideHandicapTimeScoringInteractor() {
        // TODO: obtain scoring method from event
        // TODO: obtain cone penalty from event
        return new FlatConePenaltyHandicapTimeScoringInteractor(2L);
    }
}
