package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.List;

public interface SortingStrategy {
    void sort(List<Movie> movies, Comparator<Movie> comp);
}
