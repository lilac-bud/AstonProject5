package com.github.lilacbud.astonproject5.movie;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieTest {
    private Movie movie;

    @BeforeEach
    public void setUp() {
        movie = new Movie.Builder()
                .withName("Криминальное чтиво")
                .withYearOfRelease(1994)
                .withHourLength(2.5F)
                .build();
    }
    
    @Test
    public void testCreateMovie() {
        System.out.println("Movie");
        assertEquals("Криминальное чтиво", movie.getName());
        assertEquals(1994, movie.getYearOfRelease());
        assertEquals(2.5F, movie.getHourLength());
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        String expected = String.format("Movie {Name: %s Year of release: %d Hour length: %f }", 
                "Криминальное чтиво", 1994, 2.5F);
        assertEquals(expected, movie.toString());
    }

    @Test
    public void createMovieWithNullName() {
        System.err.println("Movie with null name");
        Movie movieWithNullName = new Movie.Builder()
                .withName(null)
                .withYearOfRelease(1994)
                .withHourLength(2.5F)
                .build();
        assertEquals("", movieWithNullName.getName());
    }
}