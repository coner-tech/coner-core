package org.coner;

import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.gateway.*;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.*;
import org.coner.resource.*;

import com.fasterxml.jackson.databind.*;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.*;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConerDropwizardApplicationTest {

    // Test constants
    private static final int NUMBER_OF_RESOURCES = 12;

    // application dependencies
    @Mock
    private ConerDropwizardDependencyContainer dependencies;
    @Mock
    private HibernateBundle<ConerDropwizardConfiguration> hibernate;
    @Mock
    private ConerCoreService conerCoreService;
    @Mock
    private EventApiDomainBoundary eventBoundary;
    @Mock
    private EventDao eventDao;
    @Mock
    private EventGateway eventGateway;
    @Mock
    private RegistrationApiDomainBoundary registrationBoundary;
    @Mock
    private RegistrationDao registrationDao;
    @Mock
    private RegistrationGateway registrationGateway;
    @Mock
    private HandicapGroupApiDomainBoundary handicapGroupBoundary;
    @Mock
    private HandicapGroupDao handicapGroupDao;
    @Mock
    private HandicapGroupGateway handicapGroupGateway;
    @Mock
    private CompetitionGroupApiDomainBoundary competitionGroupBoundary;
    @Mock
    private CompetitionGroupDao competitionGroupDao;
    @Mock
    private CompetitionGroupGateway competitionGroupGateway;


    // initialize method parameters
    @Mock
    private Bootstrap<ConerDropwizardConfiguration> bootstrap;
    @Mock
    private ObjectMapper objectMapper;

    // run method parameters
    @Mock
    private ConerDropwizardConfiguration config;
    @Mock
    private DataSourceFactory dataSourceFactory;
    @Mock
    private Environment environment;
    @Mock
    private JerseyEnvironment jersey;

    private ConerDropwizardApplication application;

    @Before
    public void setup() {
        application = new ConerDropwizardApplication();
        application.setDependencies(dependencies);

        when(dependencies.getHibernate()).thenReturn(hibernate);
        when(dependencies.getConerCoreService()).thenReturn(conerCoreService);
        when(dependencies.getEventApiDomainBoundary()).thenReturn(eventBoundary);
        when(dependencies.getEventDao()).thenReturn(eventDao);
        when(dependencies.getEventGateway()).thenReturn(eventGateway);
        when(dependencies.getRegistrationApiDomainBoundary()).thenReturn(registrationBoundary);
        when(dependencies.getRegistrationDao()).thenReturn(registrationDao);
        when(dependencies.getRegistrationGateway()).thenReturn(registrationGateway);
        when(dependencies.getHandicapGroupApiDomainBoundary()).thenReturn(handicapGroupBoundary);
        when(dependencies.getHandicapGroupDao()).thenReturn(handicapGroupDao);
        when(dependencies.getHandicapGroupGateway()).thenReturn(handicapGroupGateway);
        when(dependencies.getCompetitionGroupApiDomainBoundary()).thenReturn(competitionGroupBoundary);
        when(dependencies.getCompetitionGroupDao()).thenReturn(competitionGroupDao);
        when(dependencies.getCompetitionGroupGateway()).thenReturn(competitionGroupGateway);

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
        assertThat(registeredClasses).contains(HandicapGroupResource.class);
        assertThat(registeredClasses).contains(HandicapGroupSetsResource.class);
        assertThat(registeredClasses).contains(CompetitionGroupsResource.class);
        assertThat(registeredClasses).contains(CompetitionGroupResource.class);
        assertThat(registeredClasses).contains(CompetitionGroupSetsResource.class);
        assertThat(registeredClasses).contains(WebApplicationExceptionMapper.class);
    }
}
