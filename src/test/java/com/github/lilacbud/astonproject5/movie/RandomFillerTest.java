package com.github.lilacbud.astonproject5.movie;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RandomFillerTest {
    
    private final Collection<Movie> movies = new ArrayList<>();
    public RandomFillerTest() {
    }
    
    @AfterEach
    public void tearDown() {
        movies.clear();
    }

    @Test
    public void testFillMoviesWithSizeZero() {
        System.out.println("fillMovies with size set to zero");
        RandomFiller instance = new RandomFiller(0);
        instance.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }
    @Test
    public void testFillMoviesWithSizeLessThanZero() {
        System.out.println("fillMovies with size set to less than zero");
        RandomFiller instance = new RandomFiller(-10);
        instance.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }
    @Test
    public void testFillMoviesWithSizeMoreThanZero() {
        System.out.println("fillMovies with size set to more than zero");
        RandomFiller instance = new RandomFiller(10);
        instance.fillMovies(movies);
        assertTrue(movies.size() == 10 && !movies.contains(null));
    }
}
