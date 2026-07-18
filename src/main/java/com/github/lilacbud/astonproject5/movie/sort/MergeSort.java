package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import static java.util.Objects.requireNonNull;

public class MergeSort implements SortingStrategy {

    @Override
    public void sort(List<Movie> movies, Comparator<Movie> comp) {
        if (requireNonNull(movies, "Movies must not be null").size() <= 1) {
            return;
        }
        final List<Movie> result = doMergeSort(movies, comp);
        movies.clear();
        movies.addAll(result);
    }

    private List<Movie> doMergeSort(List<Movie> movies, Comparator<Movie> comp) {
        final int size = movies.size();
        if (size <= 1) {
            return movies;
        }
        final int middle = size / 2;
        final List<Movie> left = doMergeSort(movies.subList(0, middle), comp);
        final List<Movie> right = doMergeSort(movies.subList(middle, size), comp);
        return merge(left, right, comp);
    }

    private List<Movie> merge(List<Movie> left, List<Movie> right, Comparator<Movie> comp) {
        final List<Movie> result = new ArrayList<>();
        final ListIterator<Movie> leftIt = left.listIterator(), rightIt = right.listIterator();
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
