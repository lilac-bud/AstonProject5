package com.github.lilacbud.astonproject5.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie=new Movie.Builder()
                .withName("Криминальное чтиво")
                .withYearOfRelease(1994)
                .withHourLength(2.5f)
                .build();
    }

    @Test
    void getName() {
        assertEquals("Криминальное чтиво", movie.getName());
    }

    @Test
    void getYearOfRelease() {
        assertEquals(1994, movie.getYearOfRelease());
    }

    @Test
    void getHourLength() {
        assertEquals(2.5f, movie.getHourLength());
    }

    @Test
    void testToString() {
        String expected = String.format("Movie {название=%-35s год=%d длительность=%.1f ч }", "Криминальное чтиво", 1994, 2.5f);
        assertEquals(expected, movie.toString());
    }
}