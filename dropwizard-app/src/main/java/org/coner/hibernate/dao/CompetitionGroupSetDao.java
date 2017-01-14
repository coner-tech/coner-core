package org.coner.hibernate.dao;

import java.util.List;

import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class CompetitionGroupSetDao
        extends AbstractDAO<CompetitionGroupSetHibernateEntity>
        implements HibernateEntityDao<CompetitionGroupSetHibernateEntity> {

    public CompetitionGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void create(CompetitionGroupSetHibernateEntity entity) {
        persist(entity);
    }

    @Override
    public List<CompetitionGroupSetHibernateEntity> findAll() {
        return list(namedQuery(CompetitionGroupSetHibernateEntity.QUERY_FIND_ALL));
    }

    @Override
    public CompetitionGroupSetHibernateEntity findById(String id) {
        return get(id);
    }
}
