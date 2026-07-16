package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoviesSorterTest {
    private List<Movie> movies;
    private Comparator<Movie> comparatorByName;
    private SortingStrategy testStrategy;
    private boolean sortWasCalled;
    
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

    @BeforeEach
    public void setUp() {
        movies = new ArrayList<>(List.of(movie1, movie2, movie3));
        comparatorByName = Comparator.comparing(Movie::getName);
        sortWasCalled = false;
        testStrategy = (movieList, comp)->{
            sortWasCalled=true;
            ((List<Movie>)movieList).sort(comp);
        };
    }

    @Test
    public void testSetSortingStrategy() {
        configureMovieMocksName();
        System.out.println("setSortingStrategy");
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        boolean[] newStrategyCalled = {false};
        SortingStrategy newStrategy = (movieList, comp)->{
            newStrategyCalled[0]=true;
            ((List<Movie>)movieList).sort(comp);
        };
        sorter.setSortingStrategy(newStrategy);
        sorter.performSorting(movies);

        assertTrue(newStrategyCalled[0], "Вызов новой стратегии");
        assertFalse(sortWasCalled, "Вызов старой стратегии");
    }

    @Test
    public void testSetComparator() {
        configureMovieMocksYear();
        System.out.println("setComparator");
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        Comparator<Movie> comparatorByYear = Comparator.comparing(Movie::getYearOfRelease);
        sorter.setComparator(comparatorByYear);
        sorter.performSorting(movies);
        List<Integer> years=movies.stream().map(Movie::getYearOfRelease).toList();
        assertEquals(List.of(1994,2010,2014),years);
    }

    @Test
    public void testPerformSorting() {
        configureMovieMocksName();
        System.out.println("performSorting");
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        sorter.performSorting(movies);
        List<String> names = movies.stream().map(Movie::getName).toList();
        assertEquals(List.of("Интерстеллар", "Криминальное чтиво", "Начало"), names);
        assertTrue(sortWasCalled, "Вызов стратегии сортировки");
    }

    @Test
    public void testPerformSortingForStrategyIsNull() {
        System.out.println("performSorting with null sorting strategy");
        MoviesSorter sorter = new MoviesSorter(null, comparatorByName);

        var thrown = assertThrows(NullPointerException.class, () -> sorter.performSorting(movies));

        assertEquals("Sorting strategy must be non null to perform sorting", thrown.getMessage());
    }

    @Test
    public void testPerformSortingForComparatorIsNull() {
        System.out.println("performSorting with null comparator");
        MoviesSorter sorter = new MoviesSorter(testStrategy, null);

        var thrown = assertThrows(NullPointerException.class, () -> sorter.performSorting(movies));

        assertEquals("Comparator must be non null to perform sorting", thrown.getMessage());
    }
}