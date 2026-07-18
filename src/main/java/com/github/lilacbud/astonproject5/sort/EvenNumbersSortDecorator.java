package com.github.lilacbud.astonproject5.sort;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EvenNumbersSortDecorator<E> extends SortDecorator<E> {
    private final IntegerFieldExtractor<E> extractor;

    public EvenNumbersSortDecorator(SortingStrategy<E> sortingStrategy, IntegerFieldExtractor<E> extractor) {
        super(sortingStrategy);
        this.extractor = requireNonNull(extractor, "Extractor must not be null");
    }

    @Override
    public void sort(List<E> movies, Comparator<E> comp) {
        if (requireNonNull(movies, "Movies must not be null").isEmpty()) {
            return;
        }
        final List<Integer> evenIndices = IntStream.range(0, movies.size()).boxed()
                .filter(i -> numberIsEven(movies.get(i)))
                .toList();
        final List<E> evenResult = evenIndices.stream()
                .map(movies::get)
                .collect(Collectors.toList());
        
        sortingStrategy.sort(evenResult, comp);
        Iterator<E> evenResultIt = evenResult.iterator();
        evenIndices.forEach(i -> movies.set(i, evenResultIt.next()));
    }

    private boolean numberIsEven(E movie) {
        return extractor.getValue(movie) % 2 == 0;
    }

    @FunctionalInterface
    public static interface IntegerFieldExtractor<T> {
        int getValue(T movie);
    }
}
