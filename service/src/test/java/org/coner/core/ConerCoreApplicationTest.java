package org.coner.core;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.coner.core.ConerCoreApplication;
import org.coner.core.ConerCoreConfiguration;
import org.coner.core.dagger.DaggerMockitoJerseyRegistrationComponent;
import org.coner.core.dagger.JerseyRegistrationComponent;
import org.coner.core.dagger.MockitoJerseyRegistrationModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
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

    @InjectMocks
    private ConerCoreApplication application;

    @Before
    public void setup() {
        // initialize method
        when(bootstrap.getObjectMapper()).thenReturn(objectMapper);

        // run method
        when(environment.jersey()).thenReturn(jersey);
        when(config.getDataSourceFactory()).thenReturn(dataSourceFactory);

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
                components.eventResource(),
                components.eventsResource(),
                components.eventRegistrationResource(),
                components.eventRegistrationsResource(),
                components.handicapGroupResource(),
                components.handicapGroupsResource(),
                components.handicapGroupSetsResource(),
                components.competitionGroupResource(),
                components.competitionGroupsResource(),
                components.competitionGroupSetResource(),
                components.competitionGroupSetsResource(),
                components.webApplicationExceptionMapper()
        };

        application.run(config, environment);

        Arrays.stream(expectedComponents).forEach(o -> verify(jersey).register(o));
        verify(jersey, times(expectedComponents.length)).register(Matchers.isNotNull());
    }
}
