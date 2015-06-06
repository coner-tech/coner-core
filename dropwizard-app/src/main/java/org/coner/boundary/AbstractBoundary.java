package org.coner.boundary;

import org.coner.api.entity.ApiEntity;
import org.coner.core.domain.DomainEntity;
import org.coner.hibernate.entity.HibernateEntity;

import com.google.common.base.Preconditions;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AbstractBoundary provides sensible default behaviors for most boundary traversals, including instantiating
 * an entity, converting a single entity in any supported direction, and converting a list of entities in any
 * supported direction.
 * <p>
 * The implementation details of the conversion are left for the concrete implementation in the form of building the
 * required `AbstractBoundary.EntityMerger` instance, which should be returned from concrete implementations of the
 * abstract build*Merger() methods.
 * <p>
 * The following conversions are supported:
 * <ul>
 * <li>API to Domain</li>
 * <li>Domain to API</li>
 * <li>Domain to Hibernate</li>
 * <li>Hibernate to Domain</li>
 * </ul>
 * <p>
 * Intentionally absent from this list are "API to Hibernate" and "Hibernate to API".
 *
 * @param <A> the API entity type
 * @param <D> the Domain entity type
 * @param <H> the Hibernate entity type
 */
public abstract class AbstractBoundary<A extends ApiEntity, D extends DomainEntity, H extends HibernateEntity> {

    private final Class<A> apiClass;
    private final Class<D> domainClass;
    private final Class<H> hibernateClass;
    private EntityMerger<A, D> apiToDomainMerger;
    private EntityMerger<D, A> domainToApiMerger;
    private EntityMerger<D, H> domainToHibernateMerger;
    private EntityMerger<H, D> hibernateToDomainMerger;

    public AbstractBoundary() {
        Type[] typeParameters = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.apiClass = (Class<A>) typeParameters[0];
        this.domainClass = (Class<D>) typeParameters[1];
        this.hibernateClass = (Class<H>) typeParameters[2];
    }

    private EntityMerger<D, A> getDomainToApiMerger() {
        if (domainToApiMerger == null) {
            domainToApiMerger = buildDomainToApiMerger();
        }
        return domainToApiMerger;
    }

    public A toApiEntity(D domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        A apiEntity = instantiate(apiClass);
        getDomainToApiMerger().merge(domainEntity, apiEntity);
        return apiEntity;
    }

    private EntityMerger<A, D> getApiToDomainMerger() {
        if (apiToDomainMerger == null) {
            apiToDomainMerger = buildApiToDomainMerger();
        }
        return apiToDomainMerger;
    }

    public D toDomainEntity(A apiEntity) {
        if (apiEntity == null) {
            return null;
        }
        D domainEntity = instantiate(domainClass);
        getApiToDomainMerger().merge(apiEntity, domainEntity);
        return domainEntity;
    }

    private EntityMerger<H, D> getHibernateToDomainMerger() {
        if (hibernateToDomainMerger == null) {
            hibernateToDomainMerger = buildHibernateToDomainMerger();
        }
        return hibernateToDomainMerger;
    }

    public D toDomainEntity(H hibernateEntity) {
        if (hibernateEntity == null) {
            return null;
        }
        D domainEntity = instantiate(domainClass);
        getHibernateToDomainMerger().merge(hibernateEntity, domainEntity);
        return domainEntity;
    }

    private EntityMerger<D, H> getDomainToHibernateMerger() {
        if (domainToHibernateMerger == null) {
            domainToHibernateMerger = buildDomainToHibernateMerger();
        }
        return domainToHibernateMerger;
    }

    public H toHibernateEntity(D domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        H hibernateEntity = instantiate(hibernateClass);
        getDomainToHibernateMerger().merge(domainEntity, hibernateEntity);
        return hibernateEntity;
    }

    protected <T> T instantiate(Class<T> classToInstantiate) {
        Constructor<T> constructor = null;
        try {
            constructor = classToInstantiate.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<A> toApiEntities(List<D> domainEntities) {
        if (domainEntities == null || domainEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<A> apiEntities = new ArrayList<>(domainEntities.size());
        apiEntities.addAll(domainEntities.stream().map(this::toApiEntity).collect(Collectors.toList()));
        return apiEntities;
    }

    public List<D> toDomainEntities(List<H> hibernateEntities) {
        if (hibernateEntities == null || hibernateEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<D> domainEntities = new ArrayList<>(hibernateEntities.size());
        domainEntities.addAll(hibernateEntities.stream().map(this::toDomainEntity).collect(Collectors.toList()));
        return domainEntities;
    }

    public List<H> toHibernateEntities(List<D> domainEntities) {
        if (domainEntities == null || domainEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<H> hibernateEntities = new ArrayList<>(domainEntities.size());
        hibernateEntities.addAll(domainEntities.stream().map(this::toHibernateEntity).collect(Collectors.toList()));
        return hibernateEntities;
    }

    public void merge(A fromApiEntity, D intoDomainEntity) {
        Preconditions.checkNotNull(fromApiEntity);
        Preconditions.checkNotNull(intoDomainEntity);
        getApiToDomainMerger().merge(fromApiEntity, intoDomainEntity);
    }

    public void merge(D fromDomainEntity, A intoApiEntity) {
        Preconditions.checkNotNull(fromDomainEntity);
        Preconditions.checkNotNull(intoApiEntity);
        getDomainToApiMerger().merge(fromDomainEntity, intoApiEntity);
    }

    public void merge(D fromDomainEntity, H intoHibernateEntity) {
        Preconditions.checkNotNull(fromDomainEntity);
        Preconditions.checkNotNull(intoHibernateEntity);
        getDomainToHibernateMerger().merge(fromDomainEntity, intoHibernateEntity);
    }

    public void merge(H fromHibernateEntity, D intoDomainEntity) {
        Preconditions.checkNotNull(fromHibernateEntity);
        Preconditions.checkNotNull(intoDomainEntity);
        getHibernateToDomainMerger().merge(fromHibernateEntity, intoDomainEntity);
    }

    protected abstract EntityMerger<A, D> buildApiToDomainMerger();

    protected abstract EntityMerger<D, A> buildDomainToApiMerger();

    protected abstract EntityMerger<D, H> buildDomainToHibernateMerger();

    protected abstract EntityMerger<H, D> buildHibernateToDomainMerger();

}
