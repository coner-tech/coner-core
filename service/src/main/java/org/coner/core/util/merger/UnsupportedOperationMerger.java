package org.coner.core.util.merger;

public class UnsupportedOperationMerger<S, D> implements ObjectMerger<S, D> {
    @Override
    public void merge(S source, D destination) {
        throw new UnsupportedOperationException();
    }
}
