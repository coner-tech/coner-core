package org.coner.core.gateway;

import java.util.List;

import org.coner.boundary.AbstractBoundary;
import org.coner.core.domain.entity.DomainEntity;
import org.coner.core.domain.payload.DomainAddPayload;
import org.coner.hibernate.dao.HibernateEntityDao;
import org.coner.hibernate.entity.HibernateEntity;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public abstract class AbstractGateway<
        DE extends DomainEntity,
        HE extends HibernateEntity,
        AP extends DomainAddPayload,
        EB extends AbstractBoundary<HE, DE>,
        APB extends AbstractBoundary<HE, AP>,
        D extends HibernateEntityDao<HE>>
        implements Gateway<DE, AP> {

    protected final EB entityBoundary;
    protected final APB addPayloadBoundary;
    protected final D dao;

    protected AbstractGateway(EB entityBoundary, APB addPayloadBoundary, D dao) {
        this.entityBoundary = entityBoundary;
        this.addPayloadBoundary = addPayloadBoundary;
        this.dao = dao;
    }

//    @Override
//    public void create(DE domainEntity) {
//        Preconditions.checkNotNull(domainEntity);
//        HE hibernateEntity = entityBoundary.toLocalEntity(domainEntity);
//        dao.create(hibernateEntity);
//        entityBoundary.mergeLocalIntoRemote(hibernateEntity, domainEntity);
//    }

    public DE add(AP payload) {
        Preconditions.checkNotNull(payload);
        HE hibernateEntity = addPayloadBoundary.toLocalEntity(payload);
        dao.create(hibernateEntity);
        return entityBoundary.toRemoteEntity(hibernateEntity);
    }

    @Override
    public List<DE> getAll() {
        List<HE> hibernateEntities = dao.findAll();
        return entityBoundary.toRemoteEntities(hibernateEntities);
    }

    @Override
    public DE findById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id must not be null or empty");
        HE hibernateEntity = dao.findById(id);
        return entityBoundary.toRemoteEntity(hibernateEntity);
    }
}
