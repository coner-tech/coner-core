package org.coner.boundary;

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
