package org.coner.core.gateway;

import java.util.List;

import org.coner.core.domain.entity.DomainEntity;
import org.coner.core.domain.payload.DomainAddPayload;
import org.coner.core.hibernate.dao.HibernateEntityDao;
import org.coner.core.hibernate.entity.HibernateEntity;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public abstract class MapStructAbstractGateway<
        AP extends DomainAddPayload,
        DE extends DomainEntity,
        HE extends HibernateEntity,
        D extends HibernateEntityDao<HE>>
        implements Gateway<DE, AP> {

    protected final D dao;
    protected final Mapper<AP, HE> domainAddPayloadToHibernateEntityMapper;
    protected final Mapper<HE, DE> hibernateEntityToDomainEntityMapper;
    protected final Mapper<List<HE>, List<DE>> hibernateEntitiesToDomainEntitiesMapper;

    protected MapStructAbstractGateway(
            Mapper<AP, HE> domainAddPayloadToHibernateEntityMapper,
            Mapper<HE, DE> hibernateEntityToDomainEntityMapper,
            Mapper<List<HE>, List<DE>> hibernateEntitiesToDomainEntitiesMapper,
            D dao
    ) {
        this.domainAddPayloadToHibernateEntityMapper = domainAddPayloadToHibernateEntityMapper;
        this.hibernateEntityToDomainEntityMapper = hibernateEntityToDomainEntityMapper;
        this.hibernateEntitiesToDomainEntitiesMapper = hibernateEntitiesToDomainEntitiesMapper;
        this.dao = dao;
    }

    public DE add(AP payload) {
        Preconditions.checkNotNull(payload);
        HE hibernateEntity = domainAddPayloadToHibernateEntityMapper.map(payload);
        dao.create(hibernateEntity);
        return hibernateEntityToDomainEntityMapper.map(hibernateEntity);
    }

    @Override
    public List<DE> getAll() {
        List<HE> hibernateEntities = dao.findAll();
        return hibernateEntitiesToDomainEntitiesMapper.map(hibernateEntities);
    }

    @Override
    public DE findById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id must not be null or empty");
        HE hibernateEntity = dao.findById(id);
        return hibernateEntityToDomainEntityMapper.map(hibernateEntity);
    }

    public interface Mapper<S, T> {
        T map(S s);
    }
}
