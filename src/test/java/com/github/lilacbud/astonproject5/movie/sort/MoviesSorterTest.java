package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
class MoviesSorterTest {
    private List<Movie> movies;
    private Comparator<Movie> comparatorByName;
    private SortingStrategy testStrategy;
    private boolean sortWasCalled;

    @BeforeEach
    void setUp() {
        movies = new ArrayList<>(List.of(
                new Movie.Builder().withName("Криминальное чтиво").withYearOfRelease(1994).withHourLength(2.5f).build(),
                new Movie.Builder().withName("Интерстеллар").withYearOfRelease(2014).withHourLength(3).build(),
                new Movie.Builder().withName("Начало").withYearOfRelease(2010).withHourLength(2.5f).build()
        ));
        comparatorByName = Comparator.comparing(Movie::getName);
        sortWasCalled = false;
        testStrategy = (movies, comp)->{
            sortWasCalled=true;
            ((List<Movie>)movies).sort(comp);
        };
    }

    @Test
    void setSortingStrategy() {
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        boolean[] newStrategyCalled = {false};
        SortingStrategy newStrategy = (movies, comp)->{
            newStrategyCalled[0]=true;
            ((List<Movie>)movies).sort(comp);
        };
        sorter.setSortingStrategy(newStrategy);
        sorter.performSorting(movies);

        assertTrue(newStrategyCalled[0], "Вызов новой стратегии");
        assertFalse(sortWasCalled, "Вызов старой стратегии");
    }

    @Test
    void setComparator() {
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        Comparator<Movie> comparatorByYear = Comparator.comparing(Movie::getYearOfRelease);
        sorter.setComparator(comparatorByYear);
        sorter.performSorting(movies);
        List<Integer> years=movies.stream().map(Movie::getYearOfRelease).toList();
        assertEquals(List.of(1994,2010,2014),years);
    }

    @Test
    void performSorting() {
        MoviesSorter sorter = new MoviesSorter(testStrategy, comparatorByName);
        sorter.performSorting(movies);
        List<String> names = movies.stream().map(Movie::getName).toList();
        assertEquals(List.of("Интерстеллар", "Криминальное чтиво", "Начало"), names);
        assertTrue(sortWasCalled, "Вызов стратегии сортировки");
    }
}