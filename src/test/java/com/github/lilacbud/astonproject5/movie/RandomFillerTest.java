package com.github.lilacbud.astonproject5.movie;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RandomFillerTest {
    private final Collection<Movie> movies = new ArrayList<>();

    @AfterEach
    public void tearDown() {
        movies.clear();
    }

    @Test
    public void testCreateRandomFillerWithSizeLessThanZero() {
        System.out.println("RandomFiller with size set to less than zero");
        var thrown = assertThrows(IllegalArgumentException.class, () -> new RandomFiller(-10));
        assertEquals(thrown.getMessage(), "Size cannot be negative");
        assertTrue(movies.isEmpty());
    }
    
    @Test
    public void testFillMoviesWithSizeZero() {
        System.out.println("fillMovies with size set to zero");
        new RandomFiller(0).fillMovies(movies);
        assertTrue(movies.isEmpty());
    }
    
    @Test
    public void testFillMoviesWithSizeMoreThanZero() {
        System.out.println("fillMovies with size set to more than zero");
        new RandomFiller(10).fillMovies(movies);
        assertEquals(movies.size(), 10);
        assertFalse(movies.contains(null));
    }

    @Test
    void testFillMoviesWithNullMoviesArgument() {
        System.out.println("fillMovies with null movies argument");
        RandomFiller rf = new RandomFiller(10);
        var thrown = assertThrows(NullPointerException.class, () -> rf.fillMovies(null));
        assertEquals("Movies must not be null", thrown.getMessage());
    }
}
