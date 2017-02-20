package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class EventMapperTest {

    private EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddEventRequest addEventRequest = new AddEventRequest();
        addEventRequest.setName(TestConstants.EVENT_NAME);
        addEventRequest.setDate(TestConstants.EVENT_DATE);
        EventAddPayload expected = new EventAddPayload();
        expected.setName(TestConstants.EVENT_NAME);
        expected.setDate(TestConstants.EVENT_DATE);

        EventAddPayload actual = mapper.toDomainAddPayload(addEventRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromEventDomainEntity() {
        Event event = DomainEntityTestUtils.fullDomainEvent();
        EventApiEntity expected = ApiEntityTestUtils.fullApiEvent();

        EventApiEntity actual = mapper.toApiEntity(event);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntitiesListFromDomainEntitiesList() {
        List<Event> events = Arrays.asList(DomainEntityTestUtils.fullDomainEvent());
        List<EventApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullApiEvent());

        List<EventApiEntity> actual = mapper.toApiEntityList(events);

        assertThat(actual).isEqualTo(expected);
    }
}
