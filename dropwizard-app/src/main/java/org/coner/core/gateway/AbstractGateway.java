package org.coner.core.gateway;

import org.coner.boundary.AbstractBoundary;
import org.coner.core.domain.DomainEntity;
import org.coner.hibernate.dao.HibernateEntityDao;
import org.coner.hibernate.entity.HibernateEntity;

import com.google.common.base.*;
import java.util.List;

public abstract class AbstractGateway<
        DE extends DomainEntity,
        HE extends HibernateEntity,
        B extends AbstractBoundary<?, DE, HE>,
        D extends HibernateEntityDao<HE>> {

    private final B boundary;
    private final D dao;

    protected AbstractGateway(B boundary, D dao) {
        this.boundary = boundary;
        this.dao = dao;
    }

    public void create(DE domainEntity) {
        Preconditions.checkNotNull(domainEntity);
        HE hibernateEntity = boundary.toHibernateEntity(domainEntity);
        dao.create(hibernateEntity);
        boundary.merge(hibernateEntity, domainEntity);
    }

    public List<DE> getAll() {
        List<HE> hibernateEntities = dao.findAll();
        return boundary.toDomainEntities(hibernateEntities);
    }

    public DE findById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id must not be null or empty");
        HE hibernateEntity = dao.findById(id);
        return boundary.toDomainEntity(hibernateEntity);
    }

    protected B getBoundary() {
        return boundary;
    }

    protected D getDao() {
        return dao;
    }
}
