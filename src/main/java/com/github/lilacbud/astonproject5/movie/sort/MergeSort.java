package com.github.lilacbud.astonproject5.movie.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import static java.util.Objects.requireNonNull;

public class MergeSort<E> implements SortingStrategy<E> {

    @Override
    public void sort(List<E> movies, Comparator<E> comp) {
        if (requireNonNull(movies, "Movies must not be null").size() <= 1) {
            return;
        }
        final List<E> result = doMergeSort(movies, comp);
        movies.clear();
        movies.addAll(result);
    }

    private <E> List<E> doMergeSort(List<E> movies, Comparator<E> comp) {
        final int size = movies.size();
        if (size <= 1) {
            return movies;
        }
        final int middle = size / 2;
        final List<E> left = doMergeSort(movies.subList(0, middle), comp);
        final List<E> right = doMergeSort(movies.subList(middle, size), comp);
        return merge(left, right, comp);
    }

    private <E> List<E> merge(List<E> left, List<E> right, Comparator<E> comp) {
        final List<E> result = new ArrayList<>();
        final ListIterator<E> leftIt = left.listIterator(), rightIt = right.listIterator();
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
