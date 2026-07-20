package com.github.lilacbud.astonproject5.sort;

import com.github.lilacbud.astonproject5.app.SortingStrategy;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.requireNonNull;

public abstract class SortDecorator<E> implements SortingStrategy<E> {
    public static final String SORTSTRAT_NULL_MESSAGE = "Sorting strategy must not be null";
    
    protected final SortingStrategy<E> sortingStrategy;

    public SortDecorator(SortingStrategy<E> sortingStrategy) {
        this.sortingStrategy = requireNonNull(sortingStrategy, SORTSTRAT_NULL_MESSAGE);
    }

    @Override
    public abstract void sort(List<E> list, Comparator<E> comp);
}
