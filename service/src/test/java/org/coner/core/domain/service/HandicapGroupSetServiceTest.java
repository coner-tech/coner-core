package org.coner.core.domain.service;

import static org.coner.core.util.TestConstants.HANDICAP_GROUP_ID;
import static org.coner.core.util.TestConstants.HANDICAP_GROUP_SET_ID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.HandicapGroupSetGateway;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class HandicapGroupSetServiceTest {

    @InjectMocks
    HandicapGroupSetService service;

    @Mock
    HandicapGroupSetGateway gateway;
    @Mock
    HandicapGroupEntityService handicapGroupEntityService;

    @Test
    /**
     * @see https://github.com/caeos/coner-core/issues/157
     */
    public void testIssue157() throws EntityNotFoundException, AddEntityException {
        HandicapGroupSetAddPayload payload = mock(HandicapGroupSetAddPayload.class);
        when(payload.getHandicapGroupIds()).thenReturn(Sets.newHashSet(HANDICAP_GROUP_ID));
        HandicapGroup handicapGroup = mock(HandicapGroup.class);
        when(handicapGroupEntityService.getById(HANDICAP_GROUP_ID)).thenReturn(handicapGroup);

        service.add(payload);

        verify(payload).setHandicapGroups(argThat(handicapGroups -> handicapGroups.contains(handicapGroup)));
    }

    @Test
    public void itShouldAddEntityToHandicapGroupSet() {
        HandicapGroupSet handicapGroupSet = mock(HandicapGroupSet.class);
        when(handicapGroupSet.getId()).thenReturn(HANDICAP_GROUP_SET_ID);
        HandicapGroup handicapGroup = mock(HandicapGroup.class);
        Set<HandicapGroup> handicapGroupsSet = mock(Set.class);
        when(handicapGroupsSet.contains(handicapGroup)).thenReturn(false);
        when(handicapGroupSet.getHandicapGroups()).thenReturn(handicapGroupsSet);

        service.addToHandicapGroups(handicapGroupSet, handicapGroup);

        verify(handicapGroupsSet).add(handicapGroup);
        verify(gateway).save(TestConstants.HANDICAP_GROUP_SET_ID, handicapGroupSet);
    }
}
