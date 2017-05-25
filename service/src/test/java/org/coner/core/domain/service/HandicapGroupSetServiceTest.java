package org.coner.core.domain.service;

import static org.coner.core.util.TestConstants.HANDICAP_GROUP_ID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.HandicapGroupSetGateway;
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
    HandicapGroupSetGateway handicapGroupSetGateway;
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
}
