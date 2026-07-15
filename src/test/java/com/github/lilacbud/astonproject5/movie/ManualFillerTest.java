package com.github.lilacbud.astonproject5.movie;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class ManualFillerTest {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final String input = "Фильм1\nqwerty\n2000\nqwerty\n2\nФильм2\n2005\n2.5\nФильм3\n2010\n3\n";
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

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
        System.setOut(originalOut);
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
        System.setOut(new PrintStream(outContent));
        
        String prompt1 = "Prompt1";
        String prompt2 = "Prompt2";
        String prompt3 = "Prompt3";
        String expectedOutContent = String.format("%s%s%s", prompt1, prompt2.repeat(2), prompt3.repeat(2))
                + String.format("%s%s%s", prompt1, prompt2, prompt3).repeat(2);
        
        ManualFiller manualFiller = new ManualFiller(3, new Scanner(input), 
                new ManualFiller.Prompts(prompt1, prompt2, prompt3));
        fillMoviesMock(manualFiller, movies);
        
        assertEquals(3, movies.size());
        assertEquals(List.of("Фильм1", "Фильм2", "Фильм3"), movies.stream().map(Movie::getName).toList());
        assertEquals(List.of(2000, 2005, 2010), movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(List.of(2.0F, 2.5F, 3.0F), movies.stream().map(Movie::getHourLength).toList());
        assertEquals(expectedOutContent, outContent.toString());
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

        assertEquals("Collection<Movie> movies must be non null to fillMovies", exception.getMessage());
    }
}
