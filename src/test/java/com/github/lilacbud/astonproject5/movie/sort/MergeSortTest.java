package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    private List<Movie> movies;

    @BeforeEach
    public void setUp() {
        Movie movie1 = new Movie.Builder()
                .withName("Криминальное чтиво")
                .withYearOfRelease(1994)
                .withHourLength(2.5f)
                .build();

        Movie movie2 = new Movie.Builder()
                .withName("Интерстеллар")
                .withYearOfRelease(2014)
                .withHourLength(3f)
                .build();

        Movie movie3 = new Movie.Builder()
                .withName("Начало")
                .withYearOfRelease(2010)
                .withHourLength(2.5f)
                .build();

        movies = new ArrayList<>(List.of(movie1, movie2, movie3));
    }

    @Test
    void MergeSortNameCorrectTest() {

        Comparator<Movie> comparator = Movie.compareByName;

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());

        assertEquals("Интерстеллар", movies.get(0).getName());
        assertEquals("Криминальное чтиво", movies.get(1).getName());
        assertEquals("Начало", movies.get(2).getName());
    }

    @Test
    void MergeSortYearCorrectTest() {

        Comparator<Movie> comparator = Movie.compareByYearOfRelease;

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());

        assertEquals(1994, movies.get(0).getYearOfRelease());
        assertEquals(2010, movies.get(1).getYearOfRelease());
        assertEquals(2014, movies.get(2).getYearOfRelease());
    }

    @Test
    void MergeSortHourCorrectTest() {

        Comparator<Movie> comparator = Movie.compareByHourLength;

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());

        assertEquals(2.5f, movies.get(0).getHourLength());
        assertEquals(2.5f, movies.get(1).getHourLength());
        assertEquals(3f, movies.get(2).getHourLength());
    }

    @Test
    void MergeSortEmptyCollectionTest() {

        movies.clear();
        Comparator<Movie> comparator = Movie.compareByYearOfRelease;

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertTrue(movies.isEmpty());
    }

    @Test
    void MergeSortMoviesIsNullTest() {

        movies = null;
        Comparator<Movie> comparator = Movie.compareByYearOfRelease;

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertNull(movies);
    }

    @Test
    void MergeSortSingleElementTest() {

        movies.subList(1, movies.size()).clear();
        Comparator<Movie> comparator = Movie.compareByYearOfRelease;

        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(1, movies.size());

        assertEquals(1994, movies.get(0).getYearOfRelease());
    }
}
