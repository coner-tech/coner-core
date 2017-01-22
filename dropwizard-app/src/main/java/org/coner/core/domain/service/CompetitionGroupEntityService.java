package org.coner.core.domain.service;

import javax.inject.Inject;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.gateway.CompetitionGroupGateway;

public class CompetitionGroupEntityService extends AbstractEntityService<
        CompetitionGroup,
        CompetitionGroupAddPayload,
        CompetitionGroupGateway> {

    @Inject
    public CompetitionGroupEntityService(CompetitionGroupGateway gateway) {
        super(CompetitionGroup.class, gateway);
    }
}
