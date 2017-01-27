package org.coner.core.dagger;

import javax.inject.Singleton;

import org.hibernate.SessionFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ConerModule {

    private final SessionFactory sessionFactory;

    public ConerModule(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Provides
    @Singleton
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
