package org.coner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;
import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.hibernate.dao.EventDao;
import org.coner.core.gateway.EventGateway;
import org.coner.resource.EventRegistrationsResource;
import org.coner.resource.EventResource;
import org.coner.resource.EventsResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ConerDropwizardApplicationTest {

    // application dependencies
    private final HibernateBundle<ConerDropwizardConfiguration> hibernate = mock(HibernateBundle.class);
    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final EventDao eventDao = mock(EventDao.class);
    private final EventGateway eventGateway = mock(EventGateway.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    // initialize method parameters
    private final Bootstrap<ConerDropwizardConfiguration> bootstrap = mock(Bootstrap.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);

    // run method parameters
    private final ConerDropwizardConfiguration config = mock(ConerDropwizardConfiguration.class);
    private final DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);

    private final ConerDropwizardApplication application = new ConerDropwizardApplication();

    @Before
    public void setup() {
        application.setHibernate(hibernate);
        application.setEventBoundary(eventBoundary);
        application.setEventDao(eventDao);
        application.setEventGateway(eventGateway);
        application.setConerCoreService(conerCoreService);

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

        assertThat(serializationFeatureArgumentCaptor.getValue())
                .isEqualTo(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        assertThat(valueArgumentCaptor.getValue()).isFalse();
    }

    @Test
    public void itShouldRegisterComponents() throws Exception {
        application.run(config, environment);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(jersey, times(4)).register(argumentCaptor.capture());

        List<Object> objects = argumentCaptor.getAllValues();
        List<Class> registeredClasses = new ArrayList<>(objects.size());
        registeredClasses.addAll(objects.stream().map(Object::getClass).collect(Collectors.toList()));

        assertThat(registeredClasses).contains(EventsResource.class);
        assertThat(registeredClasses).contains(EventResource.class);
        assertThat(registeredClasses).contains(EventRegistrationsResource.class);
        assertThat(registeredClasses).contains(WebApplicationExceptionMapper.class);
    }
}
