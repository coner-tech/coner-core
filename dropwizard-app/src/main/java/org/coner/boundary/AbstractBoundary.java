package org.coner.boundary;

import com.google.common.base.Preconditions;
import org.coner.api.entity.ApiEntity;
import org.coner.core.domain.DomainEntity;
import org.coner.hibernate.entity.HibernateEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    private final EntityMerger<A, D> apiToDomainMerger;
    private final EntityMerger<D, A> domainToApiMerger;
    private final EntityMerger<D, H> domainToHibernateMerger;
    private final EntityMerger<H, D> hibernateToDomainMerger;

    /**
     * Default constructor for the AbstractBoundary.
     *
     * @param apiClass       the API entity class
     * @param domainClass    the Domain entity class
     * @param hibernateClass the Hibernate entity class
     */
    public AbstractBoundary(Class<A> apiClass, Class<D> domainClass, Class<H> hibernateClass) {
        this.apiClass = apiClass;
        this.domainClass = domainClass;
        this.hibernateClass = hibernateClass;
        this.apiToDomainMerger = buildApiToDomainMerger();
        this.domainToApiMerger = buildDomainToApiMerger();
        this.domainToHibernateMerger = buildDomainToHibernateMerger();
        this.hibernateToDomainMerger = buildHibernateToDomainMerger();
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
        domainToApiMerger.merge(domainEntity, apiEntity);
        return apiEntity;
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
        apiToDomainMerger.merge(apiEntity, domainEntity);
        return domainEntity;
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
        hibernateToDomainMerger.merge(hibernateEntity, domainEntity);
        return domainEntity;
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
        domainToHibernateMerger.merge(domainEntity, hibernateEntity);
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
        apiToDomainMerger.merge(fromApiEntity, intoDomainEntity);
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
        domainToApiMerger.merge(fromDomainEntity, intoApiEntity);
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
        domainToHibernateMerger.merge(fromDomainEntity, intoHibernateEntity);
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
        hibernateToDomainMerger.merge(fromHibernateEntity, intoDomainEntity);
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

    /**
     * An EntityMerger implementation merges a source entity into a destination entity.
     *
     * @param <F> the source entity
     * @param <T> the destination entity
     */
    public interface EntityMerger<F, T> {
        /**
         * Merge a source entity into a destination entity.
         *
         * @param fromEntity the source entity
         * @param toEntity   the destination entity
         */
        void merge(F fromEntity, T toEntity);
    }
}
