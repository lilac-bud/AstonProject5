package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.sort.MergeSort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MergeSortTest {
    @Test
    public void givenReverseIntegerComparator_whenSortingListOfIntegers_thenListShouldBeSortedInDescendingOrder() {
        final MergeSort<Integer> mergeSort = new MergeSort<>();
        final Comparator<Integer> comparator = Comparator.reverseOrder();
        final List<Integer> list = new ArrayList<>(List.of(1, 2, 3));
        final List<Integer> expectedList = List.copyOf(list.reversed());
        mergeSort.sort(list, comparator);
        assertEquals(expectedList, list);
    }

    @Test
    public void givenEmptyList_whenSortingList_thenThrowNothing() {
        final List<Integer> list = new ArrayList<>();
        MergeSort<Integer> mergeSort = new MergeSort<>();
        assertDoesNotThrow(() -> mergeSort.sort(list, Comparator.naturalOrder()));
        assertTrue(list.isEmpty());
    }

    @Test
    public void givenNullAsList_whenSortingList_thenThrow() {
        MergeSort<Integer> mergeSort = new MergeSort<>();
        var thrown = assertThrows(NullPointerException.class, () -> mergeSort.sort(null, Comparator.naturalOrder()));
        assertEquals(MergeSort.LIST_NULL_MESSAGE, thrown.getMessage());
    }

    @Test
    public void givenListOfSingleElement_whenSortingList_thenThrowNothing() {
        List<Integer> list = new ArrayList<>(List.of(1));
        List<Integer> expectedList = List.copyOf(list);
        MergeSort<Integer> mergeSort = new MergeSort<>();
        assertDoesNotThrow(() -> mergeSort.sort(list, Comparator.naturalOrder()));
        assertEquals(expectedList, list);
    }
}
