package org.axrunner.boundary;

import com.google.common.base.Preconditions;
import org.axrunner.api.entity.ApiEntity;
import org.axrunner.core.domain.DomainEntity;
import org.axrunner.hibernate.entity.HibernateEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class AbstractBoundary<A extends ApiEntity, D extends DomainEntity, H extends HibernateEntity> {

    private final Class<A> apiClass;
    private final Class<D> domainClass;
    private final Class<H> hibernateClass;
    private final EntityMerger<A, D> apiToDomainMerger;
    private final EntityMerger<D, A> domainToApiMerger;
    private final EntityMerger<D, H> domainToHibernateMerger;
    private final EntityMerger<H, D> hibernateToDomainMerger;

    public AbstractBoundary(Class<A> apiClass, Class<D> domainClass, Class<H> hibernateClass) {
        this.apiClass = apiClass;
        this.domainClass = domainClass;
        this.hibernateClass = hibernateClass;
        this.apiToDomainMerger = buildApiToDomainMerger();
        this.domainToApiMerger = buildDomainToApiMerger();
        this.domainToHibernateMerger = buildDomainToHibernateMerger();
        this.hibernateToDomainMerger = buildHibernateToDomainMerger();
    }

    public A toApiEntity(D domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        A apiEntity = instantiate(apiClass);
        domainToApiMerger.merge(domainEntity, apiEntity);
        return apiEntity;
    }

    public D toDomainEntity(A apiEntity) {
        if (apiEntity == null) {
            return null;
        }
        D domainEntity = instantiate(domainClass);
        apiToDomainMerger.merge(apiEntity, domainEntity);
        return domainEntity;
    }

    public D toDomainEntity(H hibernateEntity) {
        if (hibernateEntity == null) {
            return null;
        }
        D domainEntity = instantiate(domainClass);
        hibernateToDomainMerger.merge(hibernateEntity, domainEntity);
        return domainEntity;
    }

    public H toHibernateEntity(D domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        H hibernateEntity = instantiate(hibernateClass);
        domainToHibernateMerger.merge(domainEntity, hibernateEntity);
        return hibernateEntity;
    }

    protected <T> T instantiate(Class<T> clazz) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<A> toApiEntities(List<D> domainEntities) {
        if (domainEntities == null || domainEntities.size() == 0) {
            return new ArrayList<>();
        }
        List<A> apiEntities = new ArrayList<>(domainEntities.size());
        apiEntities.addAll(domainEntities.stream().map(this::toApiEntity).collect(Collectors.toList()));
        return apiEntities;
    }

    public List<D> toDomainEntities(List<H> hibernateEntities) {
        if (hibernateEntities == null || hibernateEntities.size() == 0) {
            return new ArrayList<>();
        }
        List<D> domainEntities = new ArrayList<>(hibernateEntities.size());
        domainEntities.addAll(hibernateEntities.stream().map(this::toDomainEntity).collect(Collectors.toList()));
        return domainEntities;
    }

    public List<H> toHibernateEntities(List<D> domainEntities) {
        if (domainEntities == null || domainEntities.size() == 0) {
            return new ArrayList<>();
        }
        List<H> hibernateEntities = new ArrayList<>(domainEntities.size());
        hibernateEntities.addAll(domainEntities.stream().map(this::toHibernateEntity).collect(Collectors.toList()));
        return hibernateEntities;
    }

    public void merge(A fromApiEntity, D intoDomainEntity) {
        Preconditions.checkNotNull(fromApiEntity);
        Preconditions.checkNotNull(intoDomainEntity);
        apiToDomainMerger.merge(fromApiEntity, intoDomainEntity);
    }

    public void merge(D fromDomainEntity, A intoApiEntity) {
        Preconditions.checkNotNull(fromDomainEntity);
        Preconditions.checkNotNull(intoApiEntity);
        domainToApiMerger.merge(fromDomainEntity, intoApiEntity);
    }

    public void merge(D fromDomainEntity, H intoHibernateEntity) {
        Preconditions.checkNotNull(fromDomainEntity);
        Preconditions.checkNotNull(intoHibernateEntity);
        domainToHibernateMerger.merge(fromDomainEntity, intoHibernateEntity);
    }

    public void merge(H fromHibernateEntity, D intoDomainEntity) {
        Preconditions.checkNotNull(fromHibernateEntity);
        Preconditions.checkNotNull(intoDomainEntity);
        hibernateToDomainMerger.merge(fromHibernateEntity, intoDomainEntity);
    }

    protected abstract EntityMerger<A, D> buildApiToDomainMerger();

    protected abstract EntityMerger<D, A> buildDomainToApiMerger();

    protected abstract EntityMerger<D, H> buildDomainToHibernateMerger();

    protected abstract EntityMerger<H, D> buildHibernateToDomainMerger();

    public interface EntityMerger<F, T> {
        public void merge(F fromEntity, T toEntity);
    }
}
