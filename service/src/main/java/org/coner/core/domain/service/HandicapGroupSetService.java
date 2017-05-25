package org.coner.core.domain.service;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.HandicapGroupSetGateway;

import com.google.common.collect.ImmutableSet;

public class HandicapGroupSetService extends AbstractEntityService<
        HandicapGroupSet,
        HandicapGroupSetAddPayload,
        HandicapGroupSetGateway> {

    private final HandicapGroupEntityService handicapGroupEntityService;

    @Inject
    public HandicapGroupSetService(
            HandicapGroupSetGateway gateway,
            HandicapGroupEntityService handicapGroupEntityService
    ) {
        super(HandicapGroupSet.class, gateway);
        this.handicapGroupEntityService = handicapGroupEntityService;
    }

    @Override
    public HandicapGroupSet add(HandicapGroupSetAddPayload addPayload) throws AddEntityException {
        ImmutableSet.Builder<HandicapGroup> handicapGroupsBuilder = ImmutableSet.builder();
        try {
            for (String handicapGroupId : addPayload.getHandicapGroupIds()) {
                handicapGroupsBuilder.add(handicapGroupEntityService.getById(handicapGroupId));
            }
        } catch (EntityNotFoundException e) {
            throw new AddEntityException(e);
        }
        addPayload.setHandicapGroups(handicapGroupsBuilder.build());
        return super.add(addPayload);
    }


}
