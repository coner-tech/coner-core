package org.coner.core.domain.service;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.gateway.CompetitionGroupGateway;

public class CompetitionGroupService extends AbstractDomainService<
        CompetitionGroup,
        CompetitionGroupAddPayload,
        CompetitionGroupGateway> {
    public CompetitionGroupService(CompetitionGroupGateway gateway) {
        super(CompetitionGroup.class, gateway);
    }
}
