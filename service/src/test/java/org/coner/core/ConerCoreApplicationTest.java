package org.coner.core;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.coner.core.dagger.DaggerMockitoJerseyRegistrationComponent;
import org.coner.core.dagger.JerseyRegistrationComponent;
import org.coner.core.dagger.MockitoJerseyRegistrationModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

@RunWith(MockitoJUnitRunner.class)
public class ConerCoreApplicationTest {

    JerseyRegistrationComponent components;

    // initialize method parameters
    @Mock
    private Bootstrap<ConerCoreConfiguration> bootstrap;
    @Mock
    private ObjectMapper objectMapper;

    // run method parameters
    @Mock
    private ConerCoreConfiguration config;
    @Mock
    private DataSourceFactory dataSourceFactory;
    @Mock
    private Environment environment;
    @Mock
    private JerseyEnvironment jersey;
    @Mock
    private AdminEnvironment adminEnvironment;

    @InjectMocks
    private ConerCoreApplication application;

    @Before
    public void setup() {
        // initialize method
        when(bootstrap.getObjectMapper()).thenReturn(objectMapper);
        when(config.getDataSourceFactory()).thenReturn(dataSourceFactory);
        // run method
        when(environment.jersey()).thenReturn(jersey);
        when(environment.admin()).thenReturn(adminEnvironment);

        components = DaggerMockitoJerseyRegistrationComponent.builder()
                .mockitoJerseyRegistrationModule(new MockitoJerseyRegistrationModule())
                .build();
        application.components = components;
    }

    @Test
    public void itShouldInitializeHibernate() {
        application.hibernateBundle = mock(HibernateBundle.class);

        application.initialize(bootstrap);

        verify(bootstrap).addBundle(application.hibernateBundle);
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
        Object[] expectedComponents = {
                components.eventsResource(),
                components.eventRegistrationsResource(),
                components.eventRunsResource(),
                components.handicapGroupsResource(),
                components.handicapGroupSetsResource(),
                components.competitionGroupsResource(),
                components.competitionGroupSetsResource(),
                components.domainServiceExceptionMapper(),
                components.runtimeExceptionUnwrappingMapper()
        };

        application.run(config, environment);

        Arrays.stream(expectedComponents).forEach(o -> verify(jersey).register(o));
        verifyNoMoreInteractions(jersey);
    }

    @Test
    public void itShouldConsultHsqlDatabaseManagerTaskForRegistration() throws Exception {
        application.run(config, environment);

        verify(components.hsqlDatabaseManagerSwingTask()).shouldRegister(anyMap());
    }

    @Test
    public void whenHsqlDatabaseManagerTaskDeclinesItShouldNeverRegister() throws Exception {
        when(components.hsqlDatabaseManagerSwingTask().shouldRegister(anyMap())).thenReturn(false);

        application.run(config, environment);

        verify(environment.admin(), never()).addTask(components.hsqlDatabaseManagerSwingTask());
    }

    @Test
    public void whenHsqlDatabaseManagerTaskAcceptsItShouldRegister() throws Exception {
        when(components.hsqlDatabaseManagerSwingTask().shouldRegister(anyMap())).thenReturn(true);

        application.run(config, environment);

        verify(environment.admin()).addTask(components.hsqlDatabaseManagerSwingTask());
    }
}
