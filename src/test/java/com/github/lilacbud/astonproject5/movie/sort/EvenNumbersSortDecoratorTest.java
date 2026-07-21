package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.sort.EvenNumbersSortDecorator;
import com.github.lilacbud.astonproject5.sort.MergeSort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EvenNumbersSortDecoratorTest {
    private final EvenNumbersSortDecorator<Integer> sortingStrategy = 
            new EvenNumbersSortDecorator<>(new MergeSort<>(), i -> i);
    
    @Test
    public void givenNullAsSortStrategy_whenCreatingDecorator_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> 
                new EvenNumbersSortDecorator<Integer>(null, i -> i));
        assertEquals(EvenNumbersSortDecorator.SORTSTRAT_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenNullAsExtractor_whenCreatingDecorator_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> 
                new EvenNumbersSortDecorator<Integer>(new MergeSort<>(), null));
        assertEquals(EvenNumbersSortDecorator.EXTRACTOR_NULL_MESSAGE, thrown.getMessage());
    }

    @Test
    public void givenNaturalOrderAsComparator_whenSortingIntegerList_thenSortOnlyEvenNumbers() {
        final List<Integer> list = new ArrayList<>(List.of(6, 3, 2, 1, 5, 4));
        final List<Integer> expectedList = List.of(2, 3, 4, 1, 5, 6);
        sortingStrategy.sort(list, Comparator.naturalOrder());
        assertEquals(expectedList, list);
    }

    @Test
    public void givenOnlyEvenList_whenSortingList_thenSort() {
        final List<Integer> evenList = new ArrayList<>(List.of(6, 2, 4));
        final List<Integer> expectedList = List.of(2, 4, 6);
        sortingStrategy.sort(evenList, Comparator.naturalOrder());
        assertEquals(expectedList, evenList);
    }

    @Test
    public void givenOnlyOddList_whenSortingList_thenDoNotSort() {
        final List<Integer> oddList = new ArrayList<>(List.of(3, 1, 5));
        final List<Integer> expectedList = List.copyOf(oddList);
        sortingStrategy.sort(oddList, Comparator.naturalOrder());
        assertEquals(expectedList, oddList);
    }

    @Test
    public void givenEmptyList_whenSortingList_thenThrowNothing() {
        final List<Integer> list = new ArrayList<>();
        assertDoesNotThrow(() -> sortingStrategy.sort(list, Comparator.naturalOrder()));
        assertTrue(list.isEmpty());
    }

    @Test
    public void givenNullAsList_whenSortingList_thenThrow() {
        final List<Integer> list = null;
        final var thrown = assertThrows(NullPointerException.class, 
                () -> sortingStrategy.sort(list, Comparator.naturalOrder()));
        assertEquals(EvenNumbersSortDecorator.LIST_NULL_MESSAGE, thrown.getMessage());
    }
}
