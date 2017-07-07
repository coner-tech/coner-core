package org.coner.core.dagger;

import org.coner.core.domain.interactor.RunScoringInteractor;

import dagger.Component;

@Component(modules = EventScoringModule.class)
public interface EventScoringComponent {

    RunScoringInteractor runScoringInteractor();
}
