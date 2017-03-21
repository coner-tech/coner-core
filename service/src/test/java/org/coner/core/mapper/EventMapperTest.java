package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.DomainPayloadTestUtils;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class EventMapperTest {

    private EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddEventRequest apiAddRequest = ApiRequestTestUtils.fullAddEvent();
        EventAddPayload expected = DomainPayloadTestUtils.fullEventAdd();

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
