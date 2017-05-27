package org.coner.core.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.HANDICAP_GROUP_ID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.hibernate.dao.HandicapGroupDao;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.mapper.HandicapGroupSetMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class HandicapGroupSetGatewayTest {


    @InjectMocks
    HandicapGroupSetGateway gateway;

    @Mock
    HandicapGroupSetMapper mapper;
    @Mock
    HandicapGroupSetDao dao;
    @Mock
    HandicapGroupDao handicapGroupDao;

    @Test
    /**
     * @see https://github.com/caeos/coner-core/issues/157
     */
    public void testIssue157() throws EntityNotFoundException, AddEntityException {
        HandicapGroupSetAddPayload payload = mock(HandicapGroupSetAddPayload.class);
        when(payload.getHandicapGroupIds()).thenReturn(Sets.newHashSet(HANDICAP_GROUP_ID));
        HandicapGroupHibernateEntity handicapGroupHibernateEntity = mock(HandicapGroupHibernateEntity.class);
        when(handicapGroupDao.findById(HANDICAP_GROUP_ID)).thenReturn(handicapGroupHibernateEntity);
        HandicapGroupSetHibernateEntity handicapGroupSetHibernateEntity = mock(HandicapGroupSetHibernateEntity.class);
        when(mapper.toHibernateEntity(payload)).thenReturn(handicapGroupSetHibernateEntity);
        HandicapGroupSet domainSetEntity = mock(HandicapGroupSet.class);
        when(mapper.toDomainEntity(handicapGroupSetHibernateEntity)).thenReturn(domainSetEntity);

        HandicapGroupSet actual = gateway.add(payload);

        verify(handicapGroupSetHibernateEntity).setHandicapGroups(
                argThat(argument -> argument.contains(handicapGroupHibernateEntity))
        );
        verify(dao).create(handicapGroupSetHibernateEntity);
        assertThat(actual).isSameAs(domainSetEntity);
    }
}
