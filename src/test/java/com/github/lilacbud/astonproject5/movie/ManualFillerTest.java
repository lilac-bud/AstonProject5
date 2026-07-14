package com.github.lilacbud.astonproject5.movie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ManualFillerTest {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private String input = "Фильм1\n2000\n2\nФильм2\n2005\n2.5\nФильм3\n2010\n3\n";

    @AfterEach
    public void tearDown() {
        movies.clear();
    }

    @Test
    public void testFillMoviesWithSizeZero() {
        System.out.println("fillMovies with size set to zero");
        ManualFiller manualFiller = new ManualFiller(0, new Scanner(input));
        manualFiller.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    public void testFillMoviesWithSizeMoreThanZero() throws Exception {
        System.out.println("fillMovies with size set to more than zero");
        ManualFiller manualFiller = new ManualFiller(3, new Scanner(input));
        manualFiller.fillMovies(movies);
        assertEquals(movies.size(), 3);
        assertEquals("Фильм1", movies.get(0).getName());
        assertEquals(2000, movies.get(0).getYearOfRelease());
        assertEquals(2.0, movies.get(0).getHourLength());
        assertEquals("Фильм2", movies.get(1).getName());
        assertEquals(2005, movies.get(1).getYearOfRelease());
        assertEquals(2.5, movies.get(1).getHourLength());
        assertEquals("Фильм3", movies.get(2).getName());
        assertEquals(2010, movies.get(2).getYearOfRelease());
        assertEquals(3.0, movies.get(2).getHourLength());
    }
}
