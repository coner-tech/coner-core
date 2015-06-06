package org.coner.hibernate.dao;

import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class CompetitionGroupDao extends AbstractDAO<CompetitionGroupHibernateEntity> {

    public CompetitionGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createOrUpdate(CompetitionGroupHibernateEntity competitionGroup) {
        persist(competitionGroup);
    }

    public List<CompetitionGroupHibernateEntity> findAll() {
        return list(namedQuery(CompetitionGroupHibernateEntity.QUERY_FIND_ALL));
    }

    public CompetitionGroupHibernateEntity findById(String id) {
        return get(id);
    }
}
