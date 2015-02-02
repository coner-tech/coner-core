package org.coner.boundary;

import com.google.common.base.Preconditions;
import org.coner.api.entity.ApiEntity;
import org.coner.core.domain.DomainEntity;
import org.coner.hibernate.entity.HibernateEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AbstractBoundary provides sensible default behaviors for most boundary traversals, including instantiating
 * an entity, converting a single entity in any supported direction, and converting a list of entities in any
 * supported direction.
 * <p/>
 * The implementation details of the conversion are left for the concrete implementation in the form of building the
 * required `AbstractBoundary.EntityMerger` instance, which should be returned from concrete implementations of the
 * abstract build*Merger() methods.
 * <p/>
 * The following conversions are supported:
 * <ul>
 * <li>API to Domain</li>
 * <li>Domain to API</li>
 * <li>Domain to Hibernate</li>
 * <li>Hibernate to Domain</li>
 * </ul>
 * <p/>
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

    /**
     * Default constructor for the AbstractBoundary.
     */
    public AbstractBoundary() {
        Type[] typeParameters = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.apiClass = (Class<A>) typeParameters[0];
        this.domainClass = (Class<D>) typeParameters[1];
        this.hibernateClass = (Class<H>) typeParameters[2];
    }

    /**
     * Get the Domain to API merger (lazy-init).
     *
     * @return the Domain to API merger
     */
    private EntityMerger<D, A> getDomainToApiMerger() {
        if (domainToApiMerger == null) {
            domainToApiMerger = buildDomainToApiMerger();
        }
        return domainToApiMerger;
    }

    /**
     * Convert a Domain entity into an API entity.
     *
     * @param domainEntity a Domain entity instance or null
     * @return an API entity or null if domainEntity is null
     */
    public A toApiEntity(D domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        A apiEntity = instantiate(apiClass);
        getDomainToApiMerger().merge(domainEntity, apiEntity);
        return apiEntity;
    }

    /**
     * Get the API to Domain merger (lazy-init).
     *
     * @return the API to Domain merger
     */
    private EntityMerger<A, D> getApiToDomainMerger() {
        if (apiToDomainMerger == null) {
            apiToDomainMerger = buildApiToDomainMerger();
        }
        return apiToDomainMerger;
    }

    /**
     * Convert an API entity into a Domain entity.
     *
     * @param apiEntity an API entity instance or null
     * @return a Domain entity or null if apiEntity is null
     */
    public D toDomainEntity(A apiEntity) {
        if (apiEntity == null) {
            return null;
        }
        D domainEntity = instantiate(domainClass);
        getApiToDomainMerger().merge(apiEntity, domainEntity);
        return domainEntity;
    }

    /**
     * Get the Hibernate to Domain merger (lazy-init).
     *
     * @return the Hibernate to Domain merger
     */
    private EntityMerger<H, D> getHibernateToDomainMerger() {
        if (hibernateToDomainMerger == null) {
            hibernateToDomainMerger = buildHibernateToDomainMerger();
        }
        return hibernateToDomainMerger;
    }

    /**
     * Convert a Hibernate entity into a Domain entity.
     *
     * @param hibernateEntity a Hibernate entity instance or null
     * @return a Domain entity or null if hibernateEntity is null
     */
    public D toDomainEntity(H hibernateEntity) {
        if (hibernateEntity == null) {
            return null;
        }
        D domainEntity = instantiate(domainClass);
        getHibernateToDomainMerger().merge(hibernateEntity, domainEntity);
        return domainEntity;
    }

    /**
     * Get the Domain to Hibernate merger (lazy-init).
     *
     * @return the Domain to Hibernate merger
     */
    private EntityMerger<D, H> getDomainToHibernateMerger() {
        if (domainToHibernateMerger == null) {
            domainToHibernateMerger = buildDomainToHibernateMerger();
        }
        return domainToHibernateMerger;
    }

    /**
     * Convert a Domain entity into a Hibernate entity.
     *
     * @param domainEntity a Domain entity instance or null
     * @return a Hibernate entity or null if domainEntity is null
     */
    public H toHibernateEntity(D domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        H hibernateEntity = instantiate(hibernateClass);
        getDomainToHibernateMerger().merge(domainEntity, hibernateEntity);
        return hibernateEntity;
    }

    /**
     * Convenience to instantiate a class using its default constructor.
     *
     * @param clazz the Class of the instance to be instantiated
     * @param <T>   the type of the instance to instantiate
     * @return a new instance of the type represented by clazz
     * @throws java.lang.RuntimeException if an instance cannot be created using the default constructor, such as if
     *                                    the constructor or class is private
     */
    protected <T> T instantiate(Class<T> clazz) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a list of API entities into a list of Domain entities.
     *
     * @param domainEntities a list of Domain entities or null. The list must not contain any null items.
     * @return a list of Domain entities or an empty list if domainEntities is null or empty
     */
    public List<A> toApiEntities(List<D> domainEntities) {
        if (domainEntities == null || domainEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<A> apiEntities = new ArrayList<>(domainEntities.size());
        apiEntities.addAll(domainEntities.stream().map(this::toApiEntity).collect(Collectors.toList()));
        return apiEntities;
    }

    /**
     * Convert a list of Hibernate entities into a list of Domain entities.
     *
     * @param hibernateEntities a list of Hibernate entities or null. The list must not contain any null items.
     * @return a list of Domain entities or an empty list if hibernateEntities is null or empty
     */
    public List<D> toDomainEntities(List<H> hibernateEntities) {
        if (hibernateEntities == null || hibernateEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<D> domainEntities = new ArrayList<>(hibernateEntities.size());
        domainEntities.addAll(hibernateEntities.stream().map(this::toDomainEntity).collect(Collectors.toList()));
        return domainEntities;
    }

    /**
     * Convert a list of Domain entities into a list of Hibernate entities.
     *
     * @param domainEntities a list of Domain entities or null. The list must not contain any null items.
     * @return a list of Hibernate entities or an empty list if domainEntities is null or empty
     */
    public List<H> toHibernateEntities(List<D> domainEntities) {
        if (domainEntities == null || domainEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<H> hibernateEntities = new ArrayList<>(domainEntities.size());
        hibernateEntities.addAll(domainEntities.stream().map(this::toHibernateEntity).collect(Collectors.toList()));
        return hibernateEntities;
    }

    /**
     * Merge an API entity into a Domain entity.
     * <p/>
     * Neither argument may be null. If you don't have a Domain entity instance yet, use
     * `AbstractBoundary.toDomainEntity` instead.
     *
     * @param fromApiEntity    the source API entity
     * @param intoDomainEntity the destination API entity
     */
    public void merge(A fromApiEntity, D intoDomainEntity) {
        Preconditions.checkNotNull(fromApiEntity);
        Preconditions.checkNotNull(intoDomainEntity);
        getApiToDomainMerger().merge(fromApiEntity, intoDomainEntity);
    }

    /**
     * Merge a Domain entity into an API entity.
     * <p/>
     * Neither argument may be null. If you don't have an API entity instance yet, use
     * `AbstractBoundary.toDomainEntity` instead.
     *
     * @param fromDomainEntity the source Domain entity
     * @param intoApiEntity    the destination API entity
     */
    public void merge(D fromDomainEntity, A intoApiEntity) {
        Preconditions.checkNotNull(fromDomainEntity);
        Preconditions.checkNotNull(intoApiEntity);
        getDomainToApiMerger().merge(fromDomainEntity, intoApiEntity);
    }

    /**
     * Merge a Domain entity into a Hibernate entity.
     * <p/>
     * Neither argument may be null. If you don't have a Hibernate entity instance yet, use
     * `AbstractBoundary.toHibernateEntity` instead.
     *
     * @param fromDomainEntity    the source Domain entity
     * @param intoHibernateEntity the destination Hibernate entity
     */
    public void merge(D fromDomainEntity, H intoHibernateEntity) {
        Preconditions.checkNotNull(fromDomainEntity);
        Preconditions.checkNotNull(intoHibernateEntity);
        getDomainToHibernateMerger().merge(fromDomainEntity, intoHibernateEntity);
    }

    /**
     * Merge a Hibernate entity into a Domain entity.
     * <p/>
     * Neither argument may be null. If you don't have a Domain entity instance yet, use
     * `AbstractBoundary.toDomainEntity` instead.
     *
     * @param fromHibernateEntity the source Hibernate entity
     * @param intoDomainEntity    the destination Domain entity
     */
    public void merge(H fromHibernateEntity, D intoDomainEntity) {
        Preconditions.checkNotNull(fromHibernateEntity);
        Preconditions.checkNotNull(intoDomainEntity);
        getHibernateToDomainMerger().merge(fromHibernateEntity, intoDomainEntity);
    }

    /**
     * Build an EntityMerger to merge an API entity into a Domain Entity.
     *
     * @return an EntityMerger to merge an API entity into a Domain Entity
     */
    protected abstract EntityMerger<A, D> buildApiToDomainMerger();

    /**
     * Build an EntityMerger to merge a Domain entity into an API entity.
     *
     * @return an EntityMerger to merge a Domain entity into an API entity
     */
    protected abstract EntityMerger<D, A> buildDomainToApiMerger();

    /**
     * Build an EntityMerger to merge a Domain entity into a Hibernate entity.
     *
     * @return an EntityMerger to merge a Domain entity into a Hibernate entity
     */
    protected abstract EntityMerger<D, H> buildDomainToHibernateMerger();

    /**
     * Build an EntityMerger to merge a Hibernate entity into a Domain entity.
     *
     * @return an EntityMerger to merge a Hibernate entity into a Domain entity
     */
    protected abstract EntityMerger<H, D> buildHibernateToDomainMerger();

}
