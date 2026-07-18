package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.requireNonNull;

public abstract class SortDecorator implements SortingStrategy {
    protected final SortingStrategy sortingStrategy;

    public SortDecorator(SortingStrategy sortingStrategy) {
        this.sortingStrategy = requireNonNull(sortingStrategy, "Sorting strategy must not be null");
    }

    @Override
    public abstract void sort(List<Movie> movies, Comparator<Movie> comp);
}
