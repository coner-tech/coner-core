package org.coner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.gateway.CompetitionGroupGateway;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.HandicapGroupGateway;
import org.coner.core.gateway.RegistrationGateway;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.CompetitionGroupDao;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.dao.HandicapGroupDao;
import org.coner.hibernate.dao.RegistrationDao;
import org.coner.resource.CompetitionGroupsResource;
import org.coner.resource.EventRegistrationResource;
import org.coner.resource.EventRegistrationsResource;
import org.coner.resource.EventResource;
import org.coner.resource.EventsResource;
import org.coner.resource.HandicapGroupsResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ConerDropwizardApplicationTest {

    // Test constants
    private static final int NUMBER_OF_RESOURCES = 7;

    // application dependencies
    private final HibernateBundle<ConerDropwizardConfiguration> hibernate = mock(HibernateBundle.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    // Event
    @Mock
    EventBoundary eventBoundary;
    @Mock
    EventDao eventDao;
    @Mock
    EventGateway eventGateway;

    // Registration
    @Mock
    RegistrationBoundary registrationBoundary;
    @Mock
    RegistrationDao registrationDao;
    @Mock
    RegistrationGateway registrationGateway;

    // HandicapGroup
    private final HandicapGroupBoundary handicapGroupBoundary = mock(HandicapGroupBoundary.class);
    private final HandicapGroupDao handicapGroupDao = mock(HandicapGroupDao.class);
    private final HandicapGroupGateway handicapGroupGateway = mock(HandicapGroupGateway.class);

    // Competition Group
    @Mock
    CompetitionGroupBoundary competitionGroupBoundary;
    @Mock
    CompetitionGroupDao competitionGroupDao;
    @Mock
    CompetitionGroupGateway competitionGroupGateway;


    // initialize method parameters
    @Mock
    Bootstrap<ConerDropwizardConfiguration> bootstrap;
    @Mock
    ObjectMapper objectMapper;

    // run method parameters
    @Mock
    ConerDropwizardConfiguration config;
    @Mock
    DataSourceFactory dataSourceFactory;
    @Mock
    Environment environment;
    @Mock
    JerseyEnvironment jersey;

    private final ConerDropwizardApplication application = new ConerDropwizardApplication();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        application.setHibernate(hibernate);
        application.setConerCoreService(conerCoreService);
        application.setEventBoundary(eventBoundary);
        application.setEventDao(eventDao);
        application.setEventGateway(eventGateway);
        application.setRegistrationBoundary(registrationBoundary);
        application.setRegistrationDao(registrationDao);
        application.setRegistrationGateway(registrationGateway);
        application.setHandicapGroupBoundary(handicapGroupBoundary);
        application.setHandicapGroupDao(handicapGroupDao);
        application.setHandicapGroupGateway(handicapGroupGateway);
        application.setCompetitionGroupBoundary(competitionGroupBoundary);
        application.setCompetitionGroupDao(competitionGroupDao);
        application.setCompetitionGroupGateway(competitionGroupGateway);

        // initialize method
        when(bootstrap.getObjectMapper()).thenReturn(objectMapper);

        // run method
        when(environment.jersey()).thenReturn(jersey);
        when(config.getDataSourceFactory()).thenReturn(dataSourceFactory);
    }

    @Test
    public void itShouldInitializeHibernate() {
        application.initialize(bootstrap);

        verify(bootstrap).addBundle(hibernate);
    }

    @Test
    public void itShouldWriteDatesAsTimestamps() {
        application.initialize(bootstrap);

        ArgumentCaptor<SerializationFeature> serializationFeatureArgumentCaptor = ArgumentCaptor.forClass(
                SerializationFeature.class
        );
        ArgumentCaptor<Boolean> valueArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(objectMapper).configure(serializationFeatureArgumentCaptor.capture(), valueArgumentCaptor.capture());

        assertThat(serializationFeatureArgumentCaptor.getValue())
                .isEqualTo(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        assertThat(valueArgumentCaptor.getValue()).isFalse();
    }

    @Test
    public void itShouldRegisterComponents() throws Exception {
        application.run(config, environment);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(jersey, times(NUMBER_OF_RESOURCES)).register(argumentCaptor.capture());

        List<Object> objects = argumentCaptor.getAllValues();
        List<Class> registeredClasses = new ArrayList<>(objects.size());
        registeredClasses.addAll(objects.stream().map(Object::getClass).collect(Collectors.toList()));

        assertThat(registeredClasses).contains(EventsResource.class);
        assertThat(registeredClasses).contains(EventResource.class);
        assertThat(registeredClasses).contains(EventRegistrationsResource.class);
        assertThat(registeredClasses).contains(EventRegistrationResource.class);
        assertThat(registeredClasses).contains(HandicapGroupsResource.class);
        assertThat(registeredClasses).contains(CompetitionGroupsResource.class);
        assertThat(registeredClasses).contains(WebApplicationExceptionMapper.class);
    }
}
