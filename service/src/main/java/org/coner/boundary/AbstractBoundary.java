package org.coner.boundary;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.coner.util.merger.ObjectMerger;

import com.google.common.base.Preconditions;

/**
 * AbstractBoundary provides sensible default behaviors for most boundary traversals, including instantiating
 * an entity, converting a single entity in any supported direction, and converting a list of entities in any
 * supported direction.<br/>
 * The implementation details of the conversion are left for the concrete implementation in the form of building the
 * required `AbstractBoundary.EntityMerger` instance, which should be returned from concrete implementations of the
 * abstract build*Merger() methods.
 *
 * @param <L> the local entity type
 * @param <R> the remote entity type
 */
public abstract class AbstractBoundary<L, R> {

    private final Class<L> localClass;
    private final Class<R> remoteClass;
    private ObjectMerger<L, R> localToRemoteMerger;
    private ObjectMerger<R, L> remoteToLocalMerger;

    public AbstractBoundary() {
        Type[] typeParameters = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.localClass = (Class<L>) typeParameters[0];
        this.remoteClass = (Class<R>) typeParameters[1];
    }

    private ObjectMerger<L, R> getLocalToRemoteMerger() {
        if (localToRemoteMerger == null) {
            localToRemoteMerger = buildLocalToRemoteMerger();
        }
        return localToRemoteMerger;
    }

    public R toRemoteEntity(L localEntity) {
        if (localEntity == null) {
            return null;
        }
        R remoteEntity = instantiate(remoteClass);
        getLocalToRemoteMerger().merge(localEntity, remoteEntity);
        return remoteEntity;
    }

    private ObjectMerger<R, L> getRemoteToLocalMerger() {
        if (remoteToLocalMerger == null) {
            remoteToLocalMerger = buildRemoteToLocalMerger();
        }
        return remoteToLocalMerger;
    }

    public L toLocalEntity(R remoteEntity) {
        if (remoteEntity == null) {
            return null;
        }
        L localEntity = instantiate(localClass);
        getRemoteToLocalMerger().merge(remoteEntity, localEntity);
        return localEntity;
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

    public List<R> toRemoteEntities(List<L> localEntities) {
        if (localEntities == null || localEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<R> remoteEntities = new ArrayList<>(localEntities.size());
        remoteEntities.addAll(localEntities.stream().map(this::toRemoteEntity).collect(Collectors.toList()));
        return remoteEntities;
    }

    public List<L> toLocalEntities(List<R> remoteEntities) {
        if (remoteEntities == null || remoteEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<L> localEntities = new ArrayList<>(remoteEntities.size());
        localEntities.addAll(remoteEntities.stream().map(this::toLocalEntity).collect(Collectors.toList()));
        return localEntities;
    }

    public void mergeLocalIntoRemote(L fromLocalEntity, R intoRemoteEntity) {
        Preconditions.checkNotNull(fromLocalEntity);
        Preconditions.checkNotNull(intoRemoteEntity);
        getLocalToRemoteMerger().merge(fromLocalEntity, intoRemoteEntity);
    }

    public void mergeRemoteIntoLocal(R fromRemoteEntity, L intoLocalEntity) {
        Preconditions.checkNotNull(fromRemoteEntity);
        Preconditions.checkNotNull(intoLocalEntity);
        getRemoteToLocalMerger().merge(fromRemoteEntity, intoLocalEntity);
    }

    protected abstract ObjectMerger<L, R> buildLocalToRemoteMerger();

    protected abstract ObjectMerger<R, L> buildRemoteToLocalMerger();
}
