package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MergeSortTest {

    private List<Movie> movies;

    private static final Movie movie1 = mock(Movie.class);
    private static final Movie movie2 = mock(Movie.class);
    private static final Movie movie3 = mock(Movie.class);

    @BeforeAll
    public static void setUpClass() throws Exception {
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        when(movie1.getYearOfRelease()).thenReturn(1994);
        when(movie1.getHourLength()).thenReturn(2.5f);

        when(movie2.getName()).thenReturn("Интерстеллар");
        when(movie2.getYearOfRelease()).thenReturn(2014);
        when(movie2.getHourLength()).thenReturn(3f);

        when(movie3.getName()).thenReturn("Начало");
        when(movie3.getYearOfRelease()).thenReturn(2010);
        when(movie3.getHourLength()).thenReturn(2.5f);
    }

    @BeforeEach
    public void setUp() {
        movies = new ArrayList<>(List.of(movie1, movie2, movie3));
    }

    @Test
    void mergeSortNameCorrectTest() {

        Comparator<Movie> comparator = Comparator.comparing(Movie::getName);


        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie2,movie1,movie3), movies);
    }

    @Test
    void mergeSortYearCorrectTest() {

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie1,movie3,movie2), movies);
    }

    @Test
    void mergeSortHourCorrectTest() {

        Comparator<Movie> comparator = Comparator.comparing(Movie::getHourLength);

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie1,movie3,movie2), movies);
    }

    @Test
    void mergeSortIdenticalValuesCorrectTest() {

        Comparator<Movie> comparator = Comparator.comparing(Movie::getHourLength);

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());

        assertEquals(List.of(movie1,movie3,movie2), movies);
    }

    @Test
    void mergeSortEmptyCollectionTest() {

        movies.clear();
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertTrue(movies.isEmpty());
    }

    @Test
    void mergeSortMoviesIsNullTest() {

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy mergeSort = new MergeSort();
        var thrown = assertThrows(NullPointerException.class, () -> mergeSort.sort(null, comparator));

        assertEquals("Collection<Movie> movies must be non null to sort", thrown.getMessage());
    }

    @Test
    void mergeSortSingleElementTest() {

        movies.subList(1, movies.size()).clear();
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(1, movies.size());
        assertEquals(List.of(movie1), movies);
    }
}
