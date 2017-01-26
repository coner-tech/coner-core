package org.coner.core.domain.service;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.gateway.HandicapGroupGateway;

public class HandicapGroupEntityService extends AbstractEntityService<
        HandicapGroup,
        HandicapGroupAddPayload,
        HandicapGroupGateway> {

    @Inject
    public HandicapGroupEntityService(HandicapGroupGateway gateway) {
        super(HandicapGroup.class, gateway);
    }
}
