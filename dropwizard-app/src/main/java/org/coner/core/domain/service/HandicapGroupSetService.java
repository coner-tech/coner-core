package org.coner.core.domain.service;

import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.gateway.HandicapGroupSetGateway;

public class HandicapGroupSetService extends AbstractDomainService<HandicapGroupSet, HandicapGroupSetGateway> {
    public HandicapGroupSetService(HandicapGroupSetGateway gateway) {
        super(gateway);
    }
}
