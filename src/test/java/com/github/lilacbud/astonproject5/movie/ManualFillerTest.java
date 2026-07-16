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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ManualFillerTest {
    @Mock
    private Movie movie;
    
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final String input = "Фильм1\nqwerty\n2000\nqwerty\n2\nФильм2\n2005\n2.5\nФильм3\n2010\n3\n";
    private final List<String> expectedNames = List.of("Фильм1", "Фильм2", "Фильм3");
    private final List<Integer> expectedYears = List.of(2000, 2005, 2010);
    private final List<Float> expectedLengths = List.of(2.0F, 2.5F, 3.0F);
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private void configureMovieMock() {
        when(movie.getName()).thenReturn("Фильм1", "Фильм2", "Фильм3");
        when(movie.getYearOfRelease()).thenReturn(2000, 2005, 2010);
        when(movie.getHourLength()).thenReturn(2.0F, 2.5F, 3.0F);
    }
    
    private ManualFiller createFillerWithMockedBuilder(int size, Scanner scanner, ManualFiller.Prompts prompts) {
        try (MockedConstruction<Movie.Builder> mockBuilder = mockConstruction(Movie.Builder.class, 
                    withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS))) {
            ManualFiller mf = new ManualFiller(size, scanner, prompts);
            
            Movie.Builder builder = mockBuilder.constructed().get(0);
            for (int i = 0; i < 3; ++i)
                when(builder.withName(expectedNames.get(i))
                        .withYearOfRelease(expectedYears.get(i))
                        .withHourLength(expectedLengths.get(i))
                        .build())
                        .thenReturn(movie);
            return mf;
        }
    }
    
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
        configureMovieMock();
        System.out.println("fillMovies with size set to more than zero");
        System.setOut(new PrintStream(outContent));
        
        String prompt1 = "Prompt1";
        String prompt2 = "Prompt2";
        String prompt3 = "Prompt3";
        String expectedOutContent = String.format("%s%s%s", prompt1, prompt2.repeat(2), prompt3.repeat(2))
                + String.format("%s%s%s", prompt1, prompt2, prompt3).repeat(2);
        
        ManualFiller manualFiller = createFillerWithMockedBuilder(3, new Scanner(input), 
                new ManualFiller.Prompts(prompt1, prompt2, prompt3));
        fillMoviesMock(manualFiller, movies);
        
        assertEquals(3, movies.size());
        assertEquals(expectedNames, movies.stream().map(Movie::getName).toList());
        assertEquals(expectedYears, movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(expectedLengths, movies.stream().map(Movie::getHourLength).toList());
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
