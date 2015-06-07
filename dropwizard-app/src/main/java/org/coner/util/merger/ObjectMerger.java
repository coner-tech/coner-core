package org.coner.util.merger;

/**
 * An ObjectMerger implementation merges a source object into a destination object.
 *
 * @param <S> the source object
 * @param <D> the destination object
 */
public interface ObjectMerger<S, D> {
    /**
     * Merge a source object into a destination object.
     *
     * @param fromEntity the source entity
     * @param toEntity   the destination entity
     */
    void merge(S fromEntity, D toEntity);
}
