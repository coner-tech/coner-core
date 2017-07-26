package org.coner.core.dagger;

import org.coner.core.domain.value.HandicapTimeScoringMethod;

import dagger.MapKey;

@MapKey
public @interface HandicapTimeScoringMethodKey {
    HandicapTimeScoringMethod value();
}
