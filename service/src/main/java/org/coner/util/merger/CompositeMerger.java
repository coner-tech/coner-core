package org.coner.util.merger;

import com.google.common.collect.ImmutableSet;

/**
 * An ObjectMerger which allows composing a chain of ObjectMerger instances.
 *
 * @param <S> the source object type
 * @param <D> the destination object type
 */
public class CompositeMerger<S, D> implements ObjectMerger<S, D> {

    private final ImmutableSet<ObjectMerger<S, D>> mergers;

    public CompositeMerger(ObjectMerger<S, D>... mergers) {
        this.mergers = ImmutableSet.copyOf(mergers);
    }

    @Override
    public void merge(S source, D destination) {
        for (ObjectMerger<S, D> merger : mergers) {
            merger.merge(source, destination);
        }
    }
}
