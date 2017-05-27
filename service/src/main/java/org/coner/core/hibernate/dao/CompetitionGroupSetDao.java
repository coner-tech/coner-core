package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.hibernate.SessionFactory;

public class CompetitionGroupSetDao
        extends BaseHibernateEntityDao<CompetitionGroupSetHibernateEntity> {

    @Inject
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
