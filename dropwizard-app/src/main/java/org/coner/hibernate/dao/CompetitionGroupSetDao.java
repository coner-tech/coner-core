package org.coner.hibernate.dao;

import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class CompetitionGroupSetDao extends AbstractDAO<CompetitionGroupSetHibernateEntity> {

    public CompetitionGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createOrUpdate(CompetitionGroupSetHibernateEntity competitionGroupSet) {
        persist(competitionGroupSet);
    }
}
