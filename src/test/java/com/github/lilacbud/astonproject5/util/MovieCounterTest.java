package com.github.lilacbud.astonproject5.util;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieCounterTest {

    private static final Movie movie1 = mock(Movie.class);
    private static final Movie movie2 = mock(Movie.class);
    private static final Movie movie3 = mock(Movie.class);
    private static final Movie movie4 = mock(Movie.class);

    @BeforeAll
    public static void setUp(){
        when(movie1.getName()).thenReturn("Интерстеллар");
        when(movie2.getName()).thenReturn("Начало");
        when(movie3.getName()).thenReturn("Интерстеллар");
        when(movie4.getName()).thenReturn("Криминальное чтиво");
    }

    @Test
    public void countInsertNotFound() {
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4);
        int result = MovieCounter.countInsert(movies, "Дюна");
        assertEquals(0,result);
    }

    @Test
    public void countInsertListIsEmpty(){
        int result = MovieCounter.countInsert(List.of(), "Интерстеллар");
        assertEquals(0, result);
    }

    @Test
    public void countInsertTargetIsNull(){
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4);
        int result = MovieCounter.countInsert(movies, null);
        assertEquals(0, result);
    }

    @Test
    public void countInsertListIsNull(){
        int result = MovieCounter.countInsert(null, "Интерстеллар");
        assertEquals(0,result);
    }

}
