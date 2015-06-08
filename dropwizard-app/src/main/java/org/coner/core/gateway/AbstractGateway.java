package org.coner.core.gateway;

import org.coner.boundary.AbstractBoundary;
import org.coner.core.domain.entity.DomainEntity;
import org.coner.hibernate.dao.HibernateEntityDao;
import org.coner.hibernate.entity.HibernateEntity;

import com.google.common.base.*;
import java.util.List;

public abstract class AbstractGateway<
        DE extends DomainEntity,
        HE extends HibernateEntity,
        B extends AbstractBoundary<HE, DE>,
        D extends HibernateEntityDao<HE>>
        implements Gateway<DE, HE> {

    private final B boundary;
    private final D dao;

    protected AbstractGateway(B boundary, D dao) {
        this.boundary = boundary;
        this.dao = dao;
    }

    @Override
    public void create(DE domainEntity) {
        Preconditions.checkNotNull(domainEntity);
        HE hibernateEntity = boundary.toLocalEntity(domainEntity);
        dao.create(hibernateEntity);
        boundary.mergeLocalIntoRemote(hibernateEntity, domainEntity);
    }

    @Override
    public List<DE> getAll() {
        List<HE> hibernateEntities = dao.findAll();
        return boundary.toRemoteEntities(hibernateEntities);
    }

    @Override
    public DE findById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id must not be null or empty");
        HE hibernateEntity = dao.findById(id);
        return boundary.toRemoteEntity(hibernateEntity);
    }

    protected B getBoundary() {
        return boundary;
    }

    protected D getDao() {
        return dao;
    }
}
