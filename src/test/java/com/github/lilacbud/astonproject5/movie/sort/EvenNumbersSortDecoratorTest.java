package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.sort.EvenNumbersSortDecorator;
import com.github.lilacbud.astonproject5.sort.MergeSort;
import com.github.lilacbud.astonproject5.sort.SortingStrategy;
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
public class EvenNumbersSortDecoratorTest {

    private List<Movie> movies;

    @Mock
    private Movie movie1, movie2, movie3, movie4, movie5, movie6;

    private void configureOddYearMovieMocks() {
        when(movie2.getYearOfRelease()).thenReturn(2011);
        when(movie5.getYearOfRelease()).thenReturn(2001);
        when(movie6.getYearOfRelease()).thenReturn(1999);
    }
    
    private void configureEvenYearMovieMocks() {
        when(movie1.getYearOfRelease()).thenReturn(2010);
        when(movie3.getYearOfRelease()).thenReturn(1994);
        when(movie4.getYearOfRelease()).thenReturn(2008);
    }
    
    @BeforeEach
    public void setUp() {
        movies = new ArrayList<>(List.of(movie1, movie2, movie3,movie4,movie5,movie6));
    }
    
    @Test
    void createEvenNumbersSortDecoratorWithNullExtractor() {
        System.out.println("EvenNumbersSortDecorator with null extractor");
        var thrown = assertThrows(NullPointerException.class, () -> 
                new EvenNumbersSortDecorator<Movie>(new MergeSort<>(), null));
        assertEquals("Extractor must not be null", thrown.getMessage());
    }

    @Test
    void evenNumbersSortCorrectTest() {
        configureOddYearMovieMocks();
        configureEvenYearMovieMocks();
        System.out.println("sort");
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy<Movie> sortingStrategy = 
                new EvenNumbersSortDecorator<>(new MergeSort<>(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertEquals(6, movies.size());
        assertEquals(List.of(movie3,movie2,movie4,movie1,movie5,movie6), movies);
    }

    @Test
    void onlyEvenEvenNumbersSortCorrectTest() {
        configureEvenYearMovieMocks();
        System.out.println("sort given movies with even year");
        movies.clear();
        movies.addAll(List.of(movie1,movie3,movie4));

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy<Movie> sortingStrategy = 
                new EvenNumbersSortDecorator<>(new MergeSort<>(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie3,movie4,movie1), movies);
    }

    @Test
    void onlyOddEvenNumbersSortCorrectTest() {
        configureOddYearMovieMocks();
        System.out.println("sort given movies with odd year");
        movies.clear();
        movies.addAll(List.of(movie2,movie5,movie6));

        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy<Movie> sortingStrategy = 
                new EvenNumbersSortDecorator<>(new MergeSort<>(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertEquals(3, movies.size());
        assertEquals(List.of(movie2,movie5,movie6), movies);
    }

    @Test
    void evenNumbersEmptyCollectionTest() {
        System.out.println("sort given empty movies");
        movies.clear();
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy<Movie> sortingStrategy = 
                new EvenNumbersSortDecorator<>(new MergeSort<>(),Movie::getYearOfRelease);
        sortingStrategy.sort(movies, comparator);

        assertTrue(movies.isEmpty());
    }

    @Test
    void evenNumbersMoviesIsNullTest() {
        System.out.println("sort given null movies");
        movies = null;
        Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

        SortingStrategy<Movie> sortingStrategy = 
                new EvenNumbersSortDecorator<>(new MergeSort<>(),Movie::getYearOfRelease);
        var thrown = assertThrows(NullPointerException.class, () -> sortingStrategy.sort(movies, comparator));

        assertEquals("Movies must not be null", thrown.getMessage());
    }

}
