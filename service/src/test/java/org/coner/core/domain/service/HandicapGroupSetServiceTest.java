package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.coner.core.util.DomainEntityTestUtils;
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
        HandicapGroup domainEntity = DomainEntityTestUtils.fullHandicapGroup();
        when(payload.getHandicapGroupIds()).thenReturn(Sets.newHashSet(domainEntity.getId()));
        when(handicapGroupEntityService.getById(domainEntity.getId())).thenReturn(domainEntity);
        HandicapGroupSet domainSetEntity = mock(HandicapGroupSet.class);
        when(gateway.add(payload)).thenReturn(domainSetEntity);

        HandicapGroupSet actual = service.add(payload);

        verify(payload).setHandicapGroups(
                argThat(argument -> argument.contains(domainEntity))
        );
        verify(gateway).add(payload);
        assertThat(actual).isSameAs(domainSetEntity);
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
