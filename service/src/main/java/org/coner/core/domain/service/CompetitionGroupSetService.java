package org.coner.core.domain.service;

import java.util.Set;

import javax.inject.Inject;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.CompetitionGroupSetGateway;

import com.google.common.collect.ImmutableSet;

public class CompetitionGroupSetService extends AbstractEntityService<
        CompetitionGroupSet,
        CompetitionGroupSetAddPayload,
        CompetitionGroupSetGateway> {

    private final CompetitionGroupEntityService competitionGroupEntityService;

    @Inject
    public CompetitionGroupSetService(
            CompetitionGroupSetGateway gateway,
            CompetitionGroupEntityService competitionGroupEntityService) {
        super(CompetitionGroupSet.class, gateway);
        this.competitionGroupEntityService = competitionGroupEntityService;
    }

    @Override
    public CompetitionGroupSet add(CompetitionGroupSetAddPayload addPayload) throws AddEntityException {
        ImmutableSet.Builder<CompetitionGroup> competitionGroupsBuilder = ImmutableSet.builder();
        try {
            for (String competitionGroupId : addPayload.getCompetitionGroupIds()) {
                competitionGroupsBuilder.add(competitionGroupEntityService.getById(competitionGroupId));
            }
        } catch (EntityNotFoundException e) {
            throw new AddEntityException(e);
        }
        addPayload.setCompetitionGroups(competitionGroupsBuilder.build());
        return gateway.add(addPayload);
    }

    public CompetitionGroupSet addToCompetitionGroups(
            CompetitionGroupSet competitionGroupSet,
            CompetitionGroup competitionGroup
    ) {
        Set<CompetitionGroup> competitionGroups = competitionGroupSet.getCompetitionGroups();
        if (competitionGroups.contains(competitionGroup)) {
            return competitionGroupSet;
        }
        competitionGroups.add(competitionGroup);
        return gateway.save(competitionGroupSet.getId(), competitionGroupSet);
    }
}
