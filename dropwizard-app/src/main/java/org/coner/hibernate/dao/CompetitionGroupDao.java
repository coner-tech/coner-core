package org.coner.hibernate.dao;

import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class CompetitionGroupDao
        extends AbstractDAO<CompetitionGroupHibernateEntity>
        implements HibernateEntityDao<CompetitionGroupHibernateEntity> {

    public CompetitionGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void create(CompetitionGroupHibernateEntity competitionGroup) {
        persist(competitionGroup);
    }

    @Override
    public List<CompetitionGroupHibernateEntity> findAll() {
        return list(namedQuery(CompetitionGroupHibernateEntity.QUERY_FIND_ALL));
    }

    @Override
    public CompetitionGroupHibernateEntity findById(String id) {
        return get(id);
    }
}
