package com.github.lilacbud.astonproject5;
import Movie;
import java.util.Collection;
import java.util.Comparator;

public class MoviesSorter {
    private SortingStrategy sortStrategy;
    private Comparator<Movie> comp;

    public MoviesSorter(SortingStrategy sortStrategy, Comparator<Movie> comp){
        this.sortStrategy=sortStrategy;
    }
    public void setComparator(Comparator<Movie> comp){
        this.comp=comp;
    }
    public void performSorter(Collection<Movie> movies){
        sortStrategy.sory(movies,comp);
    }
}
