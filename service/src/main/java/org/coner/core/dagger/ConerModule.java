package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.ConerCoreConfiguration;
import org.coner.core.task.HsqlDatabaseManagerSwingTask;
import org.hibernate.SessionFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ConerModule {

    private final ConerCoreConfiguration configuration;
    private final SessionFactory sessionFactory;

    public ConerModule(ConerCoreConfiguration configuration, SessionFactory sessionFactory) {
        this.configuration = configuration;
        this.sessionFactory = sessionFactory;
    }

    @Provides
    @Singleton
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Provides
    @Singleton
    public HsqlDatabaseManagerSwingTask getHsqlDatabaseManagerSwingTask(SessionFactory sessionFactory) {
        return new HsqlDatabaseManagerSwingTask(configuration.getDataSourceFactory().getUrl());
    }

}
