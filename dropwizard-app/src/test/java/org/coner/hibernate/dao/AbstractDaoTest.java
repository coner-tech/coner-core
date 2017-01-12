package org.coner.hibernate.dao;

import org.coner.hibernate.entity.*;

import java.sql.*;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

public abstract class AbstractDaoTest {

    private static final String URL_PREFIX = "jdbc:hsqldb:mem:coner-";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";
    private static final String DB_DRIVER = "org.hsqldb.jdbc.JDBCDriver";

    private final Class<? extends HibernateEntity>[] hibernateEntityClasses = new Class[]{
            EventHibernateEntity.class,
            RegistrationHibernateEntity.class,
    };

    private SessionFactory sessionFactory;

    public AbstractDaoTest() {
        Configuration configuration = new Configuration();
        for (Class<? extends HibernateEntity> hibernateEntityClass : hibernateEntityClasses) {
            configuration.addAnnotatedClass(hibernateEntityClass);
        }
        configuration.setProperty(AvailableSettings.URL, URL_PREFIX + getClass().getSimpleName());
        configuration.setProperty(AvailableSettings.USER, DB_USER);
        configuration.setProperty(AvailableSettings.PASS, DB_PASS);
        configuration.setProperty(AvailableSettings.DRIVER, DB_DRIVER);
        configuration.setProperty(AvailableSettings.USE_SQL_COMMENTS, "false");
        configuration.setProperty(AvailableSettings.SHOW_SQL, "false");

        configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect");
        configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "create");
        configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        configuration.setProperty(AvailableSettings.USE_GET_GENERATED_KEYS, "true");
        configuration.setProperty(AvailableSettings.GENERATE_STATISTICS, "true");
        configuration.setProperty(AvailableSettings.USE_REFLECTION_OPTIMIZER, "true");
        configuration.setProperty(AvailableSettings.ORDER_UPDATES, "true");
        configuration.setProperty(AvailableSettings.ORDER_INSERTS, "true");
        configuration.setProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        configuration.setProperty("jadira.usertype.autoRegisterUserTypes", "true");

        sessionFactory = configuration.buildSessionFactory();
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
