package com.github.lilacbud.astonproject5.movie;

import static org.junit.jupiter.api.Assertions.*;
class MovieTest {
    private Movie movie;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        movie=new Movie.Builder()
                .withName("Криминальное чтиво")
                .withYearOfRelease(1994)
                .withHourLength(2.5f)
                .build();
    }

    @org.junit.jupiter.api.Test
    void getName() {
        assertEquals("Криминальное чтиво", movie.getName());
    }

    @org.junit.jupiter.api.Test
    void getYearOfRelease() {
        assertEquals(1994, movie.getYearOfRelease());
    }

    @org.junit.jupiter.api.Test
    void getHourLength() {
        assertEquals(2.5f, movie.getHourLength());
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String expected = String.format("Movie {название=%-35s год=%d длительность=%.1f ч }", "Криминальное чтиво", 1994, 2.5f);
        assertEquals(expected, movie.toString());
    }
}