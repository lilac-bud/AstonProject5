package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EvenNumbersSortDecorator  extends SortDecorator {
    private final IntegerFieldExtractor extractor;

    public EvenNumbersSortDecorator(SortingStrategy sortingStrategy, IntegerFieldExtractor extractor) {
        super(sortingStrategy);
        this.extractor = requireNonNull(extractor, "Extractor must not be null");
    }

    @Override
    public void sort(List<Movie> movies, Comparator<Movie> comp) {
        if (requireNonNull(movies, "Movies must not be null").isEmpty()) {
            return;
        }
        final List<Integer> evenIndices = IntStream.range(0, movies.size()).boxed()
                .filter(i -> numberIsEven(movies.get(i)))
                .toList();
        final List<Movie> evenResult = evenIndices.stream()
                .map(movies::get)
                .collect(Collectors.toList());
        
        sortingStrategy.sort(evenResult, comp);
        Iterator<Movie> evenResultIt = evenResult.iterator();
        evenIndices.forEach(i -> movies.set(i, evenResultIt.next()));
    }

    private boolean numberIsEven(Movie movie) {
        return extractor.getValue(movie) % 2 == 0;
    }

    @FunctionalInterface
    public static interface IntegerFieldExtractor {
        int getValue(Movie movie);
    }
}
