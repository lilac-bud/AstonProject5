package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Comparator;
import java.util.Collection;

public class MoviesSorter {
    private SortingStrategy sortStrategy;
    private Comparator<Movie> comp;

    public MoviesSorter(SortingStrategy sortStrategy, Comparator<Movie> comp) {
        this.sortStrategy=sortStrategy;
        this.comp = comp;
    }

    public void setSortingStrategy(SortingStrategy sortStrategy) { this.sortStrategy=sortStrategy;}
    public void setComparator(Comparator<Movie> comp){
        this.comp=comp;
    }
    public void performSorter(Collection<Movie> movies){
        sortStrategy.sort(movies,comp);
    }
}
