package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MergeSortTest {
    private List<Movie> movies;

    @Mock
    private Movie movie1, movie2, movie3;

    private void configureMovieMocksName() {
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        when(movie2.getName()).thenReturn("Интерстеллар");
        when(movie3.getName()).thenReturn("Начало");
    }
    
    private void configureMovieMocksYear() {
        when(movie1.getYearOfRelease()).thenReturn(1994);
        when(movie2.getYearOfRelease()).thenReturn(2014);
        when(movie3.getYearOfRelease()).thenReturn(2010);
    }
    
    private void configureMovieMocksLength() {
        when(movie1.getHourLength()).thenReturn(2.5f);
        when(movie2.getHourLength()).thenReturn(3f);
        when(movie3.getHourLength()).thenReturn(2.5f);
    }

    @BeforeEach
    public void setUp() {
        movies = new ArrayList<>(List.of(movie1, movie2, movie3));
    }

    @Test
    void testMergeSortNameCorrect() {
        configureMovieMocksName();
        System.out.println("sort given comparator by name");
        
        Comparator<Movie> comparator = Comparator.comparing(Movie::getName);
        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie2,movie1,movie3), movies);
    }

    @Test
    void testMergeSortYearCorrect() {
        configureMovieMocksYear();
        System.out.println("sort given comparator by year");
        
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);
        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie1,movie3,movie2), movies);
    }

    @Test
    void testMergeSortHourIdenticalValuesCorrect() {
        configureMovieMocksLength();
        System.out.println("sort given comparator by length");
        
        Comparator<Movie> comparator = Comparator.comparing(Movie::getHourLength);
        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie1,movie3,movie2), movies);
    }

    @Test
    void testMergeSortEmptyCollection() {
        System.out.println("sort given empty movies");
        
        movies.clear();
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);
        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertTrue(movies.isEmpty());
    }

    @Test
    void testMergeSortMoviesIsNull() {
        System.out.println("sort given null movies");
        
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);
        SortingStrategy mergeSort = new MergeSort();
        
        var thrown = assertThrows(NullPointerException.class, () -> mergeSort.sort(null, comparator));
        assertEquals("Movies must not be null", thrown.getMessage());
    }

    @Test
    void testMergeSortSingleElement() {
        System.out.println("sort given one element movies");
       
        movies.subList(1, movies.size()).clear();
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);
        SortingStrategy mergeSort = new MergeSort();
        mergeSort.sort(movies, comparator);

        assertEquals(1, movies.size());
        assertEquals(List.of(movie1), movies);
    }
}
