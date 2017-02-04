package org.coner.core;

import java.util.Set;

import org.coner.core.dagger.ConerModule;
import org.coner.core.dagger.DaggerJerseyRegistrationComponent;
import org.coner.core.dagger.JerseyRegistrationComponent;
import org.coner.core.hibernate.entity.HibernateEntity;
import org.coner.core.util.JacksonUtil;
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

public class ConerCoreApplication extends Application<ConerCoreConfiguration> {

    JerseyRegistrationComponent components;
    HibernateBundle<ConerCoreConfiguration> hibernateBundle;

    /**
     * The main method of the application.
     *
     * @param args raw String arguments
     * @throws Exception any uncaught exception
     */
    public static void main(String[] args) throws Exception {
        new ConerCoreApplication().run(args);
    }

    @Override
    public void initialize(
            Bootstrap<ConerCoreConfiguration> bootstrap
    ) {
        bootstrap.addBundle(getHibernateBundle());
        bootstrap.addBundle(new SwaggerBundle<ConerCoreConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                    ConerCoreConfiguration configuration
            ) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });

        JacksonUtil.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
            ConerCoreConfiguration conerCoreConfiguration,
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
        jersey.register(components.domainServiceExceptionMapper());
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

    private HibernateBundle<ConerCoreConfiguration> getHibernateBundle() {
        if (hibernateBundle == null) {
            Reflections r = new Reflections("org.coner.core.hibernate.entity");
            Set<Class<? extends HibernateEntity>> hibernateEntityClasses = r.getSubTypesOf(HibernateEntity.class);
            hibernateBundle = new HibernateBundle<ConerCoreConfiguration>(
                    ImmutableList.copyOf(hibernateEntityClasses),
                    new SessionFactoryFactory()
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(
                        ConerCoreConfiguration conerCoreConfiguration
                ) {
                    return conerCoreConfiguration.getDataSourceFactory();
                }
            };
        }
        return hibernateBundle;
    }
}

