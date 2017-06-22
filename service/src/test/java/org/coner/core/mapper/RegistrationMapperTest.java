package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.domain.service.HandicapGroupEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.hibernate.dao.RegistrationDao;
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
public class RegistrationMapperTest {

    private RegistrationMapper mapper = Mappers.getMapper(RegistrationMapper.class);

    @Mock
    RegistrationDao dao;
    @Mock
    EventMapper eventMapper;
    @Mock
    HandicapGroupMapper handicapGroupMapper;
    @Mock
    HandicapGroupEntityService handicapGroupEntityService;

    @Before
    public void setup() throws EntityNotFoundException {
        mapper.setDao(dao);
        mapper.setEventMapper(eventMapper);
        mapper.setHandicapGroupMapper(handicapGroupMapper);
        mapper.setHandicapGroupEntityService(handicapGroupEntityService);

        when(handicapGroupEntityService.getById(TestConstants.HANDICAP_GROUP_ID))
                .thenReturn(DomainEntityTestUtils.fullHandicapGroup());
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() throws EntityNotFoundException {
        AddRegistrationRequest apiAddRequest = ApiRequestTestUtils.fullAddRegistration();
        RegistrationAddPayload expected = DomainPayloadTestUtils.fullRegistrationAdd();
        expected.setEvent(null);

        RegistrationAddPayload actual = mapper.toDomainAddPayload(apiAddRequest, TestConstants.EVENT_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        Registration domainEntity = DomainEntityTestUtils.fullRegistration();
        RegistrationApiEntity expected = ApiEntityTestUtils.fullRegistration();

        RegistrationApiEntity actual = mapper.toApiEntity(domainEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityListFromDomainEntityList() {
        List<Registration> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullRegistration());
        List<RegistrationApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullRegistration());

        List<RegistrationApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
