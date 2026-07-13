package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;

import java.util.Collection;
import java.util.Comparator;

public abstract class SortDecorator implements SortingStrategy {
    protected final SortingStrategy sortingStrategy;

    protected SortDecorator(SortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    @Override
    public abstract void sort(Collection<Movie> movies, Comparator<Movie> comp);
}
