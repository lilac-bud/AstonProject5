package com.github.lilacbud.astonproject5.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.requireNonNull;

public class MoviesSorter {
    private SortingStrategy<Movie> sortStrategy;
    private Comparator<Movie> comp;

    public MoviesSorter(SortingStrategy<Movie> sortStrategy, Comparator<Movie> comp) {
        this.sortStrategy = sortStrategy;
        this.comp = comp;
    }

    public void setSortingStrategy(SortingStrategy<Movie> sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void setComparator(Comparator<Movie> comp) {
        this.comp = comp;
    }

    public void performSorting(List<Movie> movies) {
        requireNonNull(sortStrategy, "Sorting strategy must not be null to perform sorting");
        requireNonNull(comp, "Comparator must not be null to perform sorting");
        sortStrategy.sort(movies, comp);
    }
}
