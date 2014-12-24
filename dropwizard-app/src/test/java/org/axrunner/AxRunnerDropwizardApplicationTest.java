package org.axrunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.exception.ResourceExceptionMapper;
import org.axrunner.hibernate.dao.EventDao;
import org.axrunner.hibernate.gateway.EventGateway;
import org.axrunner.resource.EventResource;
import org.axrunner.resource.EventsResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AxRunnerDropwizardApplicationTest {

    // application dependencies
    private final HibernateBundle<AxRunnerDropwizardConfiguration> hibernate = mock(HibernateBundle.class);
    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final EventDao eventDao = mock(EventDao.class);
    private final EventGateway eventGateway = mock(EventGateway.class);
    private final AxRunnerCoreService axRunnerCoreService = mock(AxRunnerCoreService.class);

    // initialize method parameters
    private final Bootstrap<AxRunnerDropwizardConfiguration> bootstrap = mock(Bootstrap.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);

    // run method parameters
    private final AxRunnerDropwizardConfiguration config = mock(AxRunnerDropwizardConfiguration.class);
    private final DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);

    private final AxRunnerDropwizardApplication application = new AxRunnerDropwizardApplication();

    @Before
    public void setup() {
        application.setHibernate(hibernate);
        application.setEventBoundary(eventBoundary);
        application.setEventDao(eventDao);
        application.setEventGateway(eventGateway);
        application.setAxRunnerCoreService(axRunnerCoreService);

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

        ArgumentCaptor<SerializationFeature> serializationFeatureArgumentCaptor = ArgumentCaptor.forClass(SerializationFeature.class);
        ArgumentCaptor<Boolean> valueArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(objectMapper).configure(serializationFeatureArgumentCaptor.capture(), valueArgumentCaptor.capture());

        assertEquals(serializationFeatureArgumentCaptor.getValue(), SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        assertFalse(valueArgumentCaptor.getValue());
    }

    @Test
    public void itShouldRegisterComponents() throws Exception {
        application.run(config, environment);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(jersey, times(3)).register(argumentCaptor.capture());

        List<Object> objects = argumentCaptor.getAllValues();
        List<Class> registeredClasses = new ArrayList<>(objects.size());
        registeredClasses.addAll(objects.stream().map(Object::getClass).collect(Collectors.toList()));

        assertTrue(registeredClasses.contains(EventsResource.class));
        assertTrue(registeredClasses.contains(EventResource.class));
        assertTrue(registeredClasses.contains(ResourceExceptionMapper.class));
    }
}
