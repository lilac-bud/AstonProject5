package com.github.lilacbud.astonproject5.movie;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FromFileFillerTest {
    @Mock
    private Movie movie;
    
    private final List<String> expectedNames = List.of("The Shawshank Redemption", "The Godfather", 
            "The Matrix", "Inception", "Interstellar");
    private final List<Integer> expectedYears = List.of(1994, 1972, 1999, 2010, 2014);
    private final List<Float> expectedLengths = List.of(2.4F, 2.9F, 2.3F, 2.5F, 2.8F);
    
    private void configureMovieMock() {
        when(movie.getName()).thenReturn("The Shawshank Redemption", "The Godfather", 
                "The Matrix", "Inception", "Interstellar");
        when(movie.getYearOfRelease()).thenReturn(1994, 1972, 1999, 2010, 2014);
        when(movie.getHourLength()).thenReturn(2.4F, 2.9F, 2.3F, 2.5F, 2.8F);
    }
    
    private FromFileFiller createFiller(String filename) {
        URL resource = getClass().getClassLoader().getResource(filename);
        requireNonNull(resource, "resource must be non null to save");
        try {
            return new FromFileFiller(Paths.get(resource.toURI()).toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot load test resource: " + filename, e);
        }
    }
    
    private FromFileFiller createFillerWithMockedBuilder(String filepath) {
        try (MockedConstruction<Movie.Builder> mockBuilder = mockConstruction(Movie.Builder.class, 
                    withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS))) {
            FromFileFiller fff = createFiller(filepath);
            
            Movie.Builder builder = mockBuilder.constructed().get(0);
            for (int i = 0; i < 5; ++i)
                when(builder.withName(expectedNames.get(i))
                        .withYearOfRelease(expectedYears.get(i))
                        .withHourLength(expectedLengths.get(i))
                        .build())
                        .thenReturn(movie);
            return fff;
        }
    }

    private void fillMovies(FromFileFiller fff, Collection<Movie> movies) {
        try (MockedStatic<MovieInputValidation> validation = mockStatic(MovieInputValidation.class)) {
            validation.when(() -> MovieInputValidation.validateName(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> MovieInputValidation.validateYearOfRelease(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation.validateYearOfRelease("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> MovieInputValidation.validateHourLength(anyString()))
                    .thenAnswer(i -> Optional.of(Float.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation.validateHourLength("qwerty")).thenReturn(Optional.empty());
            
            fff.fillMovies(movies);
        }
    }

    @Test
    public void givenNullAsFilepath_whenCreatingFromFileFiller_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> new FromFileFiller(null));
        assertEquals(FromFileFiller.FILEPATH_NULL_MESSAGE, thrown.getMessage());
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void givenInvalidPath_whenCreatingFromFileFiller_thenThrow() {
        final String filename = "Name:InvalidFile.txt";
        assertThrows(InvalidPathException.class, () -> new FromFileFiller(filename));
    }
    
    @Test
    public void givenThatFileExistsNot_whenCreatingFromFileFiller_thenThrow() {
        final String filename = "NotExistFile.txt";
        final var thrown = assertThrows(IllegalArgumentException.class, () -> new FromFileFiller(filename));
        assertEquals(FromFileFiller.FILE_EXISTS_NOT_MESSAGE, thrown.getMessage());
    }

    @Test
    public void givenThatFileIsCorrect_whenFillingCollection_thenCollectionShouldBeFilled() {
        configureMovieMock();
        final String filename = "correctMovies.txt";
        final FromFileFiller fff = createFillerWithMockedBuilder(filename);
        final List<Movie> movies = new ArrayList<>();
        fillMovies(fff, movies);

        assertEquals(5, movies.size());
        assertEquals(expectedNames, movies.stream().map(Movie::getName).toList());
        assertEquals(expectedYears, movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(expectedLengths, movies.stream().map(Movie::getHourLength).toList());
    }

    @Test
    public void givenThatFileIsEmpty_whenFillingCollection_thenCollectionShouldBeEmpty() {
        final String filename = "emptyFile.txt";
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        fff.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    public void givenThatFileHasExtraFields_whenFillingCollection_thenThrow() {
        final String filename = "extraField.txt";
        final String line = "The Shawshank Redemption;1994;2.4;1972";
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_LINE_FORMAT_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenThatFileHasMissingFields_whenFillingCollection_thenThrow() {
        final String filename = "missingField.txt";
        final String line = "The Shawshank Redemption;1994";
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_LINE_FORMAT_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenThatFileHasInvalidSecondField_whenFillingCollection_thenThrow() {
        final String filename = "invalidSecondField.txt";
        final String line = "The Shawshank Redemption;qwerty;2.4";
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_VALUE_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenThatFileHasInvalidThirdField_whenFillingCollection_thenThrow() {
        final String filename = "invalidThirdField.txt";
        final String line = "The Shawshank Redemption;1994;qwerty";
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_VALUE_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenNullAsCollection_whenFillingCollection_thenThrow() {
        final String filename = "correctMovies.txt";
        final FromFileFiller fff = createFiller(filename);
        final var thrown = assertThrows(NullPointerException.class, () -> fillMovies(fff, null));
        assertEquals(FromFileFiller.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}
