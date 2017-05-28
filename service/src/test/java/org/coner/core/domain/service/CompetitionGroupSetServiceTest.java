package org.coner.core.domain.service;

import static org.coner.core.util.TestConstants.HANDICAP_GROUP_SET_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.gateway.CompetitionGroupSetGateway;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionGroupSetServiceTest {

    @InjectMocks
    CompetitionGroupSetService service;

    @Mock
    CompetitionGroupSetGateway gateway;
    @Mock
    CompetitionGroupEntityService competitionGroupEntityService;

    @Test
    public void itShouldAddEntityToCompetitionGroupSet() {
        CompetitionGroupSet competitionGroupSet = mock(CompetitionGroupSet.class);
        when(competitionGroupSet.getId()).thenReturn(HANDICAP_GROUP_SET_ID);
        CompetitionGroup competitionGroup = mock(CompetitionGroup.class);
        Set<CompetitionGroup> competitionGroupsSet = mock(Set.class);
        when(competitionGroupsSet.contains(competitionGroup)).thenReturn(false);
        when(competitionGroupSet.getCompetitionGroups()).thenReturn(competitionGroupsSet);

        service.addToCompetitionGroups(competitionGroupSet, competitionGroup);

        verify(competitionGroupsSet).add(competitionGroup);
        verify(gateway).save(TestConstants.HANDICAP_GROUP_SET_ID, competitionGroupSet);
    }
}
