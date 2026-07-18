package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Collection;
import java.util.Comparator;
import static java.util.Objects.requireNonNull;

public abstract class SortDecorator implements SortingStrategy {
    protected final SortingStrategy sortingStrategy;

    public SortDecorator(SortingStrategy sortingStrategy) {
        this.sortingStrategy = requireNonNull(sortingStrategy, "Sorting strategy must not be null");
    }

    @Override
    public abstract void sort(Collection<Movie> movies, Comparator<Movie> comp);
}
