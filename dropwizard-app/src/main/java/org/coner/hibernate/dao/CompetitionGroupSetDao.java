package org.coner.hibernate.dao;

import org.coner.hibernate.entity.CompetitionGroupSet;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class CompetitionGroupSetDao extends AbstractDAO<CompetitionGroupSet> {

    public CompetitionGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createOrUpdate(CompetitionGroupSet competitionGroupSet) {
        persist(competitionGroupSet);
    }
}
