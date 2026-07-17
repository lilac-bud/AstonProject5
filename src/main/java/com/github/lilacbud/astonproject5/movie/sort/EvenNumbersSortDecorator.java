package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.ArrayList;
import java.util.Collection;
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
    public void sort(Collection<Movie> movies, Comparator<Movie> comp) {
        if (requireNonNull(movies, "Movies must not be null").isEmpty()) {
            return;
        }
        final List<Movie> listMovies = new ArrayList<>(movies);
        final List<Integer> evenIndices = IntStream.range(0, listMovies.size()).boxed()
                .filter(i -> numberIsEven(listMovies.get(i)))
                .toList();
        final List<Movie> evenResult = evenIndices.stream()
                .map(listMovies::get)
                .collect(Collectors.toList());
        
        sortingStrategy.sort(evenResult, comp);
        Iterator<Movie> evenResultIt = evenResult.iterator();
        evenIndices.forEach(i -> listMovies.set(i, evenResultIt.next()));

        movies.clear();
        movies.addAll(listMovies);
    }

    private boolean numberIsEven(Movie movie) {
        return extractor.getValue(movie) % 2 == 0;
    }

    @FunctionalInterface
    public static interface IntegerFieldExtractor {
        int getValue(Movie movie);
    }
}
