package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.Collection;
import java.util.Objects;

public class MoviesSorter {
    private SortingStrategy sortStrategy;
    private Comparator<Movie> comp;

    public MoviesSorter(SortingStrategy sortStrategy, Comparator<Movie> comp){
        this.sortStrategy=sortStrategy;
        this.comp=comp;
    }
    public void setSortingStrategy(SortingStrategy sortStrategy){
        this.sortStrategy=sortStrategy;
    }
    public void setComparator(Comparator<Movie> comp){
        this.comp=comp;
    }
    public void performSorting(Collection<Movie> movies){

        Objects.requireNonNull(sortStrategy, "Sorting strategy must be non null to perform sorting");
        Objects.requireNonNull(comp, "Comparator must be non null to perform sorting");

        sortStrategy.sort(movies, comp);
    }
}
