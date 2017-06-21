package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.DomainPayloadTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventMapperTest {

    private EventMapper mapper;

    @Mock
    HandicapGroupSetMapper handicapGroupSetMapper;
    @Mock
    HandicapGroupSetService handicapGroupSetService;
    @Mock
    CompetitionGroupSetMapper competitionGroupSetMapper;
    @Mock
    CompetitionGroupSetService competitionGroupSetService;

    @Mock
    HandicapGroupSet handicapGroupSetDomainEntity;
    @Mock
    CompetitionGroupSet competitionGroupSetDomainEntity;

    @Before
    public void setup() {
        mapper = Mappers.getMapper(EventMapper.class);
        mapper.setHandicapGroupSetMapper(handicapGroupSetMapper);
        mapper.setHandicapGroupSetService(handicapGroupSetService);
        mapper.setCompetitionGroupSetMapper(competitionGroupSetMapper);
        mapper.setCompetitionGroupSetService(competitionGroupSetService);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() throws EntityNotFoundException {
        AddEventRequest apiAddRequest = ApiRequestTestUtils.fullAddEvent();
        EventAddPayload expected = DomainPayloadTestUtils.fullEventAdd();
        expected.setHandicapGroupSet(handicapGroupSetDomainEntity);
        expected.setCompetitionGroupSet(competitionGroupSetDomainEntity);
        when(handicapGroupSetService.getById(TestConstants.HANDICAP_GROUP_SET_ID))
                .thenReturn(handicapGroupSetDomainEntity);
        when(competitionGroupSetService.getById(TestConstants.COMPETITION_GROUP_SET_ID))
                .thenReturn(competitionGroupSetDomainEntity);

        EventAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromEventDomainEntity() {
        Event domainEntity = DomainEntityTestUtils.fullEvent();
        EventApiEntity expected = ApiEntityTestUtils.fullEvent();

        EventApiEntity actual = mapper.toApiEntity(domainEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntitiesListFromDomainEntitiesList() {
        List<Event> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullEvent());
        List<EventApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullEvent());

        List<EventApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
