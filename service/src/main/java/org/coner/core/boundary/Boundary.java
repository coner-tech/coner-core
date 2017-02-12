package org.coner.core.boundary;

import java.util.List;

public interface Boundary<L, R> {
    R toRemoteEntity(L localEntity);

    L toLocalEntity(R remoteEntity);

    List<R> toRemoteEntities(List<L> localEntities);

    List<L> toLocalEntities(List<R> remoteEntities);

    void mergeLocalIntoRemote(L fromLocalEntity, R intoRemoteEntity);

    void mergeRemoteIntoLocal(R fromRemoteEntity, L intoLocalEntity);
}
