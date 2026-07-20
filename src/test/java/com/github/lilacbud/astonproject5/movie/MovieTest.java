package com.github.lilacbud.astonproject5.movie;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MovieTest {
    @Test
    public void givenNullAsName_whenBuildingMovie_thenBuildMovieWithEmptyName() {
        Movie movie = new Movie.Builder()
                .withName(null)
                .build();
        assertTrue(movie.getName().isEmpty());
    }
}