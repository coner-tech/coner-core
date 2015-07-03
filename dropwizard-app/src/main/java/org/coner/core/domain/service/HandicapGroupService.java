package org.coner.core.domain.service;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.gateway.HandicapGroupGateway;

public class HandicapGroupService extends AbstractDomainService<
        HandicapGroup,
        HandicapGroupAddPayload,
        HandicapGroupGateway> {
    public HandicapGroupService(HandicapGroupGateway gateway) {
        super(HandicapGroup.class, gateway);
    }
}
