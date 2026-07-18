package com.github.lilacbud.astonproject5.util;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieCounterTest {
    @Mock
    private Movie movie;

    @Test
    public void testCountInsertNotFound() {
        when(movie.getName()).thenReturn("Интерстеллар", "Начало", "Интерстеллар", "Криминальное чтиво");
        System.out.println("countInsert given movies without target");
        List<Movie> movies = Collections.nCopies(4, movie);
        int result = MovieCounter.countInsert(movies, "Дюна");
        assertEquals(0,result);
    }

    @Test
    public void testCountInsertListIsEmpty(){
        System.out.println("countInsert given empty movies");
        int result = MovieCounter.countInsert(List.of(), "Интерстеллар");
        assertEquals(0, result);
    }

    @Test
    public void testCountInsertTargetIsNull(){
        System.out.println("countInsert given null target");
        List<Movie> movies = Collections.nCopies(4, movie);
        int result = MovieCounter.countInsert(movies, null);
        assertEquals(0, result);
    }

    @Test
    public void testCountInsertListIsNull(){
        System.out.println("countInsert given null movies");
        int result = MovieCounter.countInsert(null, "Интерстеллар");
        assertEquals(0,result);
    }
}
