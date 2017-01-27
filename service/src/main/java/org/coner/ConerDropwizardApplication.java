package org.coner;

import java.util.Set;

import org.coner.dagger.ConerModule;
import org.coner.dagger.DaggerJerseyRegistrationComponent;
import org.coner.dagger.JerseyRegistrationComponent;
import org.coner.hibernate.entity.HibernateEntity;
import org.coner.util.JacksonUtil;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class ConerDropwizardApplication extends Application<ConerDropwizardConfiguration> {

    JerseyRegistrationComponent components;
    HibernateBundle<ConerDropwizardConfiguration> hibernateBundle;

    /**
     * The main method of the application.
     *
     * @param args raw String arguments
     * @throws Exception any uncaught exception
     */
    public static void main(String[] args) throws Exception {
        new ConerDropwizardApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<ConerDropwizardConfiguration> bootstrap
    ) {
        bootstrap.addBundle(getHibernateBundle());
        bootstrap.addBundle(new SwaggerBundle<ConerDropwizardConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                    ConerDropwizardConfiguration configuration
            ) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });

        JacksonUtil.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
            ConerDropwizardConfiguration conerDropwizardConfiguration,
            Environment environment
    ) throws Exception {
        initComponents();
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(components.eventResource());
        jersey.register(components.eventsResource());
        jersey.register(components.eventRegistrationResource());
        jersey.register(components.eventRegistrationsResource());
        jersey.register(components.handicapGroupResource());
        jersey.register(components.handicapGroupsResource());
        jersey.register(components.handicapGroupSetsResource());
        jersey.register(components.competitionGroupResource());
        jersey.register(components.competitionGroupsResource());
        jersey.register(components.competitionGroupSetResource());
        jersey.register(components.competitionGroupSetsResource());
        jersey.register(components.webApplicationExceptionMapper());
    }

    private void initComponents() {
        if (components != null) {
            return;
        }
        ConerModule conerModule = new ConerModule(getHibernateBundle().getSessionFactory());
        components = DaggerJerseyRegistrationComponent.builder()
                .conerModule(conerModule)
                .build();
    }

    private HibernateBundle<ConerDropwizardConfiguration> getHibernateBundle() {
        if (hibernateBundle == null) {
            Reflections r = new Reflections("org.coner.hibernate.entity");
            Set<Class<? extends HibernateEntity>> hibernateEntityClasses = r.getSubTypesOf(HibernateEntity.class);
            hibernateBundle = new HibernateBundle<ConerDropwizardConfiguration>(
                    ImmutableList.copyOf(hibernateEntityClasses),
                    new SessionFactoryFactory()
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(
                        ConerDropwizardConfiguration conerDropwizardConfiguration
                ) {
                    return conerDropwizardConfiguration.getDataSourceFactory();
                }
            };
        }
        return hibernateBundle;
    }
}

