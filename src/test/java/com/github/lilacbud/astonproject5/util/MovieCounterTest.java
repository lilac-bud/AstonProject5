package com.github.lilacbud.astonproject5.util;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieCounterTest {
    @Mock
    private Movie movie;
    
    @Test
    public void giveThatListHasTarget_whenCounting_thenReturnAmount() {
        final List<String> names = List.of("Интерстеллар", "Начало", "Интерстеллар", "Криминальное чтиво");
        when(movie.getName()).thenAnswer(AdditionalAnswers.returnsElementsOf(names));
        final List<Movie> movies = Collections.nCopies(names.size(), movie);
        final String target = "Интерстеллар";
        final int expectedResult = 2;
        final int result = MovieCounter.countInsert(movies, target);
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenThatListHasNotTarget_whenCounting_thenReturnZero() {
        final List<String> names = List.of("Интерстеллар", "Начало", "Интерстеллар", "Криминальное чтиво");
        when(movie.getName()).thenAnswer(AdditionalAnswers.returnsElementsOf(names));
        final List<Movie> movies = Collections.nCopies(names.size(), movie);
        final String target = "Дюна";
        final int expectedResult = 0;
        final int result = MovieCounter.countInsert(movies, target);
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenThatListIsEmpty_whenCounting_thenReturnZero(){
        final String target = "Интерстеллар";
        final int expectedResult = 0;
        int result = MovieCounter.countInsert(List.of(), target);
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenNullAsTarget_whenCounting_thenReturnZero(){
        final String target = null;
        final List<Movie> movies = Collections.nCopies(4, movie);
        final int expectedResult = 0;
        int result = MovieCounter.countInsert(movies, target);
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenNullAsList_whenCounting_thenReturnZero(){
        final String target = "Интерстеллар";
        final List<Movie> movies = null;
        final int expectedResult = 0;
        int result = MovieCounter.countInsert(movies, target);
        assertEquals(expectedResult, result);
    }
}
