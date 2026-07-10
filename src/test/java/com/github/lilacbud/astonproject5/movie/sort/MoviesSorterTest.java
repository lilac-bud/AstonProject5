package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
class MoviesSorterTest {
    private List<Movie> movies;
    private Comparator<Movie> comparatorByName;
    private SortingStrategy testStrategy;
    private boolean sortWasCalled;
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
        comparatorByName = Comparator.comparing(Movie::getName);
        sortWasCalled = false;
        testStrategy = (movieList, comp)->{
            sortWasCalled=true;
            ((List<Movie>)movieList).sort(comp);
        };
    }

    @Test
    public void setSortingStrategy() {
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
    public void setComparator() {
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        Comparator<Movie> comparatorByYear = Comparator.comparing(Movie::getYearOfRelease);
        sorter.setComparator(comparatorByYear);
        sorter.performSorting(movies);
        List<Integer> years=movies.stream().map(Movie::getYearOfRelease).toList();
        assertEquals(List.of(1994,2010,2014),years);
    }

    @Test
    public void performSorting() {
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        sorter.performSorting(movies);
        List<String> names = movies.stream().map(Movie::getName).toList();
        assertEquals(List.of("Интерстеллар", "Криминальное чтиво", "Начало"), names);
        assertTrue(sortWasCalled, "Вызов стратегии сортировки");
    }
}