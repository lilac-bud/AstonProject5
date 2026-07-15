package com.github.lilacbud.astonproject5.movie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class ManualFillerTest {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final String input = "Фильм1\nqwerty\n2000\nqwerty\n2\nФильм2\n2005\n2.5\nФильм3\n2010\n3\n";

    private void fillMoviesMock(ManualFiller filler, Collection<Movie> movies) {
        try (MockedStatic<MovieInputValidation> validation = mockStatic(MovieInputValidation.class)) {
            validation.when(() -> MovieInputValidation.validateName(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> MovieInputValidation.validateYearOfRelease(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation.validateYearOfRelease("qwerty"))
                    .thenReturn(Optional.empty());
            validation.when(() -> MovieInputValidation.validateHourLength(anyString()))
                    .thenAnswer(i -> Optional.of(Float.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation.validateHourLength("qwerty"))
                    .thenReturn(Optional.empty());

            filler.fillMovies(movies);
        }
    }

    @AfterEach
    public void tearDown() {
        movies.clear();
    }

    @Test
    public void testFillMoviesWithSizeZero() {
        System.out.println("fillMovies with size set to zero");
        ManualFiller manualFiller = new ManualFiller(0, new Scanner(input), null);
        manualFiller.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    public void testFillMoviesWithSizeLessThanZero() {
        System.out.println("fillMovies with size set to less than zero");
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> new ManualFiller(-10, new Scanner(input), null));
        assertEquals("Size cannot be negative", thrown.getMessage());
        assertTrue(movies.isEmpty());
    }

    @Test
    public void testFillMoviesWithSizeMoreThanZero() {
        System.out.println("fillMovies with size set to more than zero");
        ManualFiller manualFiller = new ManualFiller(3, new Scanner(input), null);
        fillMoviesMock(manualFiller, movies);
        assertEquals(3, movies.size());
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

    @Test
    public void testFillMoviesWithScannerNull() {
        System.out.println("fillMovies with scanner is null");
        NullPointerException thrown =
                assertThrows(NullPointerException.class, () -> new ManualFiller(10, null, null));
        assertEquals("Scanner cannot be null", thrown.getMessage());
        assertTrue(movies.isEmpty());
    }

    @Test
    void testFillMoviesWithMoviesIsNull() {
        System.out.println("fillMovies with Movies is null");
        ManualFiller manualFiller = new ManualFiller(3, new Scanner(input), null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> manualFiller.fillMovies(null));

        assertEquals("Collection<Movie> movies must be non null to sort", exception.getMessage());
    }
}
