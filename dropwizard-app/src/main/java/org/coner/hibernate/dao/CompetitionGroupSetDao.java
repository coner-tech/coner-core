package org.coner.hibernate.dao;

import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

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
