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
    protected final Converter<AP, HE> domainAddPayloadToHibernateEntityConverter;
    protected final Merger<DE, HE> domainEntityToHibernateEntityMerger;
    protected final Converter<HE, DE> hibernateEntityToDomainEntityConverter;
    protected final Converter<List<HE>, List<DE>> hibernateEntitiesToDomainEntitiesConverter;

    protected MapStructAbstractGateway(
            Converter<AP, HE> domainAddPayloadToHibernateEntityConverter,
            Merger<DE, HE> domainEntityToHibernateEntityMerger,
            Converter<HE, DE> hibernateEntityToDomainEntityConverter,
            Converter<List<HE>, List<DE>> hibernateEntitiesToDomainEntitiesConverter,
            D dao
    ) {
        this.domainAddPayloadToHibernateEntityConverter = domainAddPayloadToHibernateEntityConverter;
        this.domainEntityToHibernateEntityMerger = domainEntityToHibernateEntityMerger;
        this.hibernateEntityToDomainEntityConverter = hibernateEntityToDomainEntityConverter;
        this.hibernateEntitiesToDomainEntitiesConverter = hibernateEntitiesToDomainEntitiesConverter;
        this.dao = dao;
    }

    public DE add(AP payload) {
        Preconditions.checkNotNull(payload);
        HE hibernateEntity = domainAddPayloadToHibernateEntityConverter.convert(payload);
        dao.create(hibernateEntity);
        return hibernateEntityToDomainEntityConverter.convert(hibernateEntity);
    }

    @Override
    public List<DE> getAll() {
        List<HE> hibernateEntities = dao.findAll();
        return hibernateEntitiesToDomainEntitiesConverter.convert(hibernateEntities);
    }

    @Override
    public DE findById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id must not be null or empty");
        HE hibernateEntity = dao.findById(id);
        return hibernateEntityToDomainEntityConverter.convert(hibernateEntity);
    }

    @Override
    public DE save(String id, DE entity) {
        Preconditions.checkArgument(entity != null, "entity must not be null");
        HE hibernateEntity = dao.findById(id);
        domainEntityToHibernateEntityMerger.merge(entity, hibernateEntity);
        dao.update(hibernateEntity);
        return hibernateEntityToDomainEntityConverter.convert(hibernateEntity);
    }

    public interface Converter<S, T> {
        T convert(S s);
    }

    public interface Merger<S, T> {
        void merge(S s, T t);
    }
}
