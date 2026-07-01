package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.Collection;

public interface SortingStrategy {
    void sort(Collection<Movie> movies, Comparator<Movie> comp);
}
