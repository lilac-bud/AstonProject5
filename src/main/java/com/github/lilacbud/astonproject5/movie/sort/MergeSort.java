package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import static java.util.Objects.requireNonNull;

public class MergeSort implements SortingStrategy {

    @Override
    public void sort(List<Movie> movies, Comparator<Movie> comp) {
        if (requireNonNull(movies, "Movies must not be null").isEmpty()) {
            return;
        }

        List<Movie> listMovies = new ArrayList<>(movies);
        List<Movie> result = doMergeSort(listMovies, comp);

        movies.clear();
        movies.addAll(result);
    }

    private List<Movie> doMergeSort(List<Movie> movies, Comparator<Movie> comp) {
        if(movies.size() <= 1) {
            return movies;
        }

        int moviesSize = movies.size();
        int middle = moviesSize / 2;

        List<Movie> left = new ArrayList<>(movies.subList(0, middle));
        List<Movie> right = new ArrayList<>(movies.subList(middle, moviesSize));

        left = doMergeSort(left, comp);
        right = doMergeSort(right, comp);

        return merge(left, right, comp);
    }

    private List<Movie> merge(List<Movie> left, List<Movie> right, Comparator<Movie> comp) {
        List<Movie> result = new LinkedList<>();
        ListIterator<Movie> leftIt = left.listIterator(), rightIt = right.listIterator();
        while (rightIt.hasNext()) {
            var rightElem = rightIt.next();
            while (leftIt.hasNext()) {
                var leftElem = leftIt.next();
                if (comp.compare(leftElem, rightElem) > 0) {
                    leftIt.previous();
                    break;
                }
                result.add(leftElem);
            }
            result.add(rightElem);
        }
        leftIt.forEachRemaining(result::add);
        return result;
    }
}
