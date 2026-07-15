package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class EvenNumbersSortDecorator  extends SortDecorator {

    private final IntegerFieldExtractor extractor;

    public EvenNumbersSortDecorator(SortingStrategy sortingStrategy, IntegerFieldExtractor extractor) {

        super(sortingStrategy);
        this.extractor = extractor;
    }

    @Override
    public void sort(Collection<Movie> movies, Comparator<Movie> comp) {

        requireNonNull(movies, "Collection<Movie> movies must be non null to sort");

        if (movies.isEmpty()) return;

        List<Movie> listMovies = new ArrayList<>(movies);

        List<Movie> evenResult = findEvenElements(listMovies);
        sortingStrategy.sort(evenResult, comp);

        int posEvenResult = 0;
        for(int i = 0; i < listMovies.size(); ++i) {
            if(numberIsEven(listMovies.get(i))) {
                listMovies.set(i, evenResult.get(posEvenResult));
                ++posEvenResult;
            }
        }

        movies.clear();
        movies.addAll(listMovies);
    }

    private boolean numberIsEven(Movie movie) {
        return extractor.getValue(movie)%2 == 0;
    }

    private List<Movie> findEvenElements(List<Movie> movies) {

        List<Movie> evenResult = new ArrayList<>();

        for(Movie movie : movies) {
            if(numberIsEven(movie)) {
                evenResult.add(movie);
            }
        }

        return evenResult;
    }

    @FunctionalInterface
    public static interface IntegerFieldExtractor {

        int getValue(Movie movie);

    }
}
