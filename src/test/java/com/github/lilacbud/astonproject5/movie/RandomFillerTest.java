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
    public void givenNegativeIntegerAsSize_whenCreatingRandomFiller_thenThrow() {
        final int size = -10;
        final var thrown = assertThrows(IllegalArgumentException.class, () -> new RandomFiller(size));
        assertEquals(RandomFiller.SIZE_NEGATIVE_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenThatRandomFillerHasZeroAsSize_whenFillingCollection_thenCollectionShouldBeEmpty() {
        final int size = 0;
        final RandomFiller rf = new RandomFiller(size);
        rf.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }
    
    @Test
    public void givenThatRandomFillerHasSizeTen_whenFillingCollection_thenFillCollectionWithTenNonNullObjects() {
        final int size = 10;
        final RandomFiller rf = new RandomFiller(size);
        rf.fillMovies(movies);
        assertEquals(size, movies.size());
        assertFalse(movies.contains(null));
    }

    @Test
    public void givenNullAsCollection_whenFillingCollection_thenThrow() {
        final int size = 10;
        final RandomFiller rf = new RandomFiller(size);
        final var thrown = assertThrows(NullPointerException.class, () -> rf.fillMovies(null));
        assertEquals(RandomFiller.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}
