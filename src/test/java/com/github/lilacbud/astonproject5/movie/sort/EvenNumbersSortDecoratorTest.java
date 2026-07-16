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

public class EvenNumbersSortDecoratorTest {

    private List<Movie> movies;

    private static final Movie movie1 = mock(Movie.class);
    private static final Movie movie2 = mock(Movie.class);
    private static final Movie movie3 = mock(Movie.class);
    private static final Movie movie4 = mock(Movie.class);
    private static final Movie movie5 = mock(Movie.class);
    private static final Movie movie6 = mock(Movie.class);

    @BeforeAll
    public static void setUpClass() throws Exception {
        when(movie1.getName()).thenReturn("movie1");
        when(movie1.getYearOfRelease()).thenReturn(2010);
        when(movie1.getHourLength()).thenReturn(2.5f);

        when(movie2.getName()).thenReturn("movie2");
        when(movie2.getYearOfRelease()).thenReturn(2011);
        when(movie2.getHourLength()).thenReturn(2.5f);

        when(movie3.getName()).thenReturn("movie3");
        when(movie3.getYearOfRelease()).thenReturn(1994);
        when(movie3.getHourLength()).thenReturn(2.5f);

        when(movie4.getName()).thenReturn("movie4");
        when(movie4.getYearOfRelease()).thenReturn(2008);
        when(movie4.getHourLength()).thenReturn(2.5f);

        when(movie5.getName()).thenReturn("movie5");
        when(movie5.getYearOfRelease()).thenReturn(2001);
        when(movie5.getHourLength()).thenReturn(2.5f);

        when(movie6.getName()).thenReturn("movie6");
        when(movie6.getYearOfRelease()).thenReturn(1999);
        when(movie6.getHourLength()).thenReturn(2.5f);
    }

    @BeforeEach
    public void setUp() {
        movies = new ArrayList<>(List.of(movie1, movie2, movie3,movie4,movie5,movie6));
    }

    @Test
    void evenNumbersSortCorrectTest() {

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy sortingStrategy = new EvenNumbersSortDecorator(new MergeSort(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertEquals(6, movies.size());
        assertEquals(List.of(movie3,movie2,movie4,movie1,movie5,movie6), movies);
    }

    @Test
    void onlyEvenEvenNumbersSortCorrectTest() {

        movies.clear();
        movies.addAll(List.of(movie1,movie3,movie4));

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy sortingStrategy = new EvenNumbersSortDecorator(new MergeSort(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie3,movie4,movie1), movies);
    }

    @Test
    void onlyOddEvenNumbersSortCorrectTest() {

        movies.clear();
        movies.addAll(List.of(movie2,movie5,movie6));

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy sortingStrategy = new EvenNumbersSortDecorator(new MergeSort(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie2,movie5,movie6), movies);
    }

    @Test
    void evenNumbersEmptyCollectionTest() {

        movies.clear();
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy sortingStrategy = new EvenNumbersSortDecorator(new MergeSort(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertTrue(movies.isEmpty());
    }

    @Test
    void evenNumbersMoviesIsNullTest() {

        movies = null;
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy sortingStrategy = new EvenNumbersSortDecorator(new MergeSort(),Movie::getYearOfRelease);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> sortingStrategy.sort(movies, comparator));

        assertEquals("Collection<Movie> movies must be non null to sort", exception.getMessage());
    }

}
