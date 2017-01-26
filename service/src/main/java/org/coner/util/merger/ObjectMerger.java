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
     * @param source      the source object
     * @param destination the destination object
     */
    void merge(S source, D destination);
}
