package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class CompetitionGroupDao
        extends AbstractDAO<CompetitionGroupHibernateEntity>
        implements HibernateEntityDao<CompetitionGroupHibernateEntity> {

    @Inject
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
