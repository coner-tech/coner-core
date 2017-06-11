package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RunHibernateEntity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class RunDao extends BaseHibernateEntityDao<RunHibernateEntity> {

    @Inject
    public RunDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void create(RunHibernateEntity entity) {
        persist(entity);
    }

    @Override
    public List<RunHibernateEntity> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RunHibernateEntity findById(String id) {
        return get(id);
    }

    public RunHibernateEntity findLastInSequenceFor(EventHibernateEntity event) {
        Class<RunHibernateEntity> runClass = RunHibernateEntity.class;
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<RunHibernateEntity> criteriaQuery = builder.createQuery(runClass);
        Root<RunHibernateEntity> runRoot = criteriaQuery.from(runClass);
        criteriaQuery.select(runRoot);
        criteriaQuery.where(builder.equal(runRoot.get("event"), event));
        criteriaQuery.orderBy(builder.desc(runRoot.get("sequence")));
        Query<RunHibernateEntity> query = currentSession().createQuery(criteriaQuery);
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
