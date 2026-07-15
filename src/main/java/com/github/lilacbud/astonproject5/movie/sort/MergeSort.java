package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class MergeSort implements SortingStrategy {

    @Override
    public void sort(Collection<Movie> movies, Comparator<Movie> comp) {

        requireNonNull(movies, "Collection<Movie> movies must be non null to sort");

        if (movies.isEmpty()) return;

        List<Movie> listMovies = new ArrayList<>(movies);
        List<Movie> result = doMergeSort(listMovies, comp);

        movies.clear();
        movies.addAll(result);
    }

    private List<Movie> doMergeSort(List<Movie> movies, Comparator<Movie> comp) {

        if(movies.size() <= 1) return movies;

        int moviesSize = movies.size();
        int middle = moviesSize/2;

        List<Movie> left = new ArrayList<>(movies.subList(0, middle));
        List<Movie> right = new ArrayList<>(movies.subList(middle, moviesSize));

        left = doMergeSort(left, comp);
        right = doMergeSort(right, comp);

        return merge(left, right, comp);
    }

    private List<Movie> merge(List<Movie> left, List<Movie> right, Comparator<Movie> comp) {

        int posLeft = 0;
        int posRight = 0;
        List<Movie> result =
                new ArrayList<>(Collections.nCopies(left.size() + right.size(), null));

        for (int k = 0; k < result.size(); k++) {
            if (posLeft == left.size()){
                result.set(k,right.get(posRight));
                ++posRight;
            } else if (posRight == right.size()) {
                result.set(k,left.get(posLeft));
                ++posLeft;
            } else if (comp.compare(left.get(posLeft), right.get(posRight)) <= 0) {
                result.set(k,left.get(posLeft));
                ++posLeft;
            } else {
                result.set(k,right.get(posRight));
                ++posRight;
            }
        }

        return result;
    }
}
