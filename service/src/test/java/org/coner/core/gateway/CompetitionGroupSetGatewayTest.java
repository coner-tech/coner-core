package org.coner.core.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.COMPETITION_GROUP_ID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.dao.CompetitionGroupSetDao;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.mapper.CompetitionGroupSetMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionGroupSetGatewayTest {

    @InjectMocks
    CompetitionGroupSetGateway gateway;

    @Mock
    CompetitionGroupSetMapper mapper;
    @Mock
    CompetitionGroupSetDao dao;
    @Mock
    CompetitionGroupDao competitionGroupDao;

    @Test
    /**
     *@see https://github.com/caeos/coner-core/issues/172
     */
    public void testIssue172() throws EntityNotFoundException, AddEntityException {
        CompetitionGroupSetAddPayload payload = mock(CompetitionGroupSetAddPayload.class);
        when(payload.getCompetitionGroupIds()).thenReturn(Sets.newHashSet(COMPETITION_GROUP_ID));
        CompetitionGroupHibernateEntity competitionGroupHibernateEntity = mock(CompetitionGroupHibernateEntity.class);
        when(competitionGroupDao.findById(COMPETITION_GROUP_ID)).thenReturn(competitionGroupHibernateEntity);
        CompetitionGroupSetHibernateEntity competitionGroupSetHibernateEntity = mock(
                CompetitionGroupSetHibernateEntity.class
        );
        when(mapper.toHibernateEntity(payload)).thenReturn(competitionGroupSetHibernateEntity);
        CompetitionGroupSet domainSetEntity = mock(CompetitionGroupSet.class);
        when(mapper.toDomainEntity(competitionGroupSetHibernateEntity)).thenReturn(domainSetEntity);

        CompetitionGroupSet actual = gateway.add(payload);

        verify(competitionGroupSetHibernateEntity).setCompetitionGroups(
                argThat(argument -> argument.contains(competitionGroupHibernateEntity))
        );
        verify(dao).create(competitionGroupSetHibernateEntity);
        assertThat(actual).isSameAs(domainSetEntity);
    }

}
