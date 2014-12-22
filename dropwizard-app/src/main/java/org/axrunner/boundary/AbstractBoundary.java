package org.axrunner.boundary;

import org.axrunner.core.domain.DomainEntity;
import org.axrunner.api.entity.ApiEntity;
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

    public AbstractBoundary(Class<A> apiClass, Class<D> domainClass, Class<H> hibernateClass) {
        this.apiClass = apiClass;
        this.domainClass = domainClass;
        this.hibernateClass = hibernateClass;
    }

    public A toApiEntity(D domainEntity) {
        A apiEntity = instantiate(apiClass);
        merge(domainEntity, apiEntity);
        return apiEntity;
    }

    public D toDomainEntity(A apiEntity) {
        D domainEntity = instantiate(domainClass);
        merge(apiEntity, domainEntity);
        return domainEntity;
    }

    public D toDomainEntity(H hibernateEntity) {
        D domainEntity = instantiate(domainClass);
        merge(hibernateEntity, domainEntity);
        return domainEntity;
    }

    public H toHibernateEntity(D domainEntity) {
        H hibernateEntity = instantiate(hibernateClass);
        merge(domainEntity, hibernateEntity);
        return hibernateEntity;
    }

    private <T> T instantiate(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
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

    public abstract void merge(A fromApiEntity, D intoDomainEntity);

    public abstract void merge(D fromDomainEntity, A intoApiEntity);

    public abstract void merge(H fromHibernateEntity, D intoDomainEntity);

    public abstract void merge(D fromDomainEntity, H intoHibernateEntity);
}
