package com.github.lilacbud.astonproject5.sort;

import com.github.lilacbud.astonproject5.app.SortingStrategy;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.requireNonNull;

public abstract class SortDecorator<E> implements SortingStrategy<E> {
    protected final SortingStrategy<E> sortingStrategy;

    public SortDecorator(SortingStrategy<E> sortingStrategy) {
        this.sortingStrategy = requireNonNull(sortingStrategy, "Sorting strategy must not be null");
    }

    @Override
    public abstract void sort(List<E> list, Comparator<E> comp);
}
