package com.github.lilacbud.astonproject5.movie;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
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
    private final int size = 3;
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
    public void givenNullAsScanner_whenCreatingManualFiller_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> new ManualFiller(size, null, null));
        assertEquals(ManualFiller.SCANNER_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenNegativeIntegerAsSize_whenCreatingManualFiller_thenThrow() {
        final var thrown = assertThrows(IllegalArgumentException.class, 
                () -> new ManualFiller(-size, new Scanner(input), null));
        assertEquals(ManualFiller.SIZE_NEGATIVE_MESSAGE, thrown.getMessage());
    }

    @Test
    public void givenThatFillerHasZeroAsSize_whenFillingCollection_thenCollectionShouldBeEmpty() {
        final int size = 0;
        final ManualFiller mf = new ManualFiller(size, new Scanner(input), null);
        mf.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }
    
    @Test
    public void givenThatFillerHasPrompts_whenFillingCollection_thenPromptsShouldBePrinted() {
        System.setOut(new PrintStream(outContent));
        final String prompt1 = "Prompt1";
        final String prompt2 = "Prompt2";
        final String prompt3 = "Prompt3";
        final String expectedOutContent = String.format("%s%s%s", prompt1, prompt2.repeat(2), prompt3.repeat(2))
                + String.format("%s%s%s", prompt1, prompt2, prompt3).repeat(2);
        final ManualFiller.Prompts prompts = new ManualFiller.Prompts(prompt1, prompt2, prompt3);
        final ManualFiller mf = createFillerWithMockedBuilder(size, new Scanner(input), prompts);
        fillMoviesMock(mf, movies);
        assertEquals(expectedOutContent, outContent.toString());
    }

    @Test
    public void givenThatFillerHasPositiveSize_whenFillingCollection_thenFillCollectionWithRequestedValues() {
        configureMovieMock();
        final ManualFiller mf = createFillerWithMockedBuilder(size, new Scanner(input), null);
        fillMoviesMock(mf, movies);
        assertEquals(size, movies.size());
        assertEquals(expectedNames, movies.stream().map(Movie::getName).toList());
        assertEquals(expectedYears, movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(expectedLengths, movies.stream().map(Movie::getHourLength).toList());
    }

    @Test
    void givenNullAsCollection_whenFillingCollection_thenThrow() {
        final ManualFiller mf = new ManualFiller(size, new Scanner(input), null);
        final var thrown = assertThrows(NullPointerException.class, () -> mf.fillMovies(null));
        assertEquals(ManualFiller.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}
