package com.github.lilacbud.astonproject5.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import static java.util.Objects.requireNonNull;

public class MergeSort<E> implements SortingStrategy<E> {

    @Override
    public void sort(List<E> list, Comparator<E> comp) {
        if (requireNonNull(list, "List must not be null").size() <= 1) {
            return;
        }
        final List<E> result = doMergeSort(list, comp);
        list.clear();
        list.addAll(result);
    }

    private <E> List<E> doMergeSort(List<E> list, Comparator<E> comp) {
        final int size = list.size();
        if (size <= 1) {
            return list;
        }
        final int middle = size / 2;
        final List<E> left = doMergeSort(list.subList(0, middle), comp);
        final List<E> right = doMergeSort(list.subList(middle, size), comp);
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
