package com.github.lilacbud.astonproject5.movie;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import java.util.Collection;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.*;

public class FromFileFillerTest {

    private FromFileFiller createFiller(String filepath) {

        URL resource = getClass().getClassLoader().getResource(filepath);

        requireNonNull(resource, "resource must be non null to save");

        try {
            return new FromFileFiller(Paths.get(resource.toURI()).toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot load test resource: " + filepath, e);
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
    void fromFileFillerFilepathIsNullTest() {

        NullPointerException exception = assertThrows(NullPointerException.class, () -> new FromFileFiller(null));
        assertEquals("Filepath must not be null", exception.getMessage());
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void fromFileFillerInvalidPathTest() {
        assertThrows(InvalidPathException.class, () -> new FromFileFiller("Name:\0InvalidFile.txt"));
    }

    @Test
    void fillMoviesCorrectTest() {

        FromFileFiller fff = createFiller("correctMovies.txt");

        List<Movie> movies = new ArrayList<>();

        fillMovies(fff, movies);

        assertEquals(5, movies.size());
        assertEquals(List.of("The Shawshank Redemption", "The Godfather", "The Matrix", "Inception", "Interstellar"),
                movies.stream().map(Movie::getName).toList());
        assertEquals(List.of(1994, 1972, 1999, 2010, 2014),
                movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(List.of(2.4F, 2.9F, 2.3F, 2.5F, 2.8F),
                movies.stream().map(Movie::getHourLength).toList());
    }

    @Test
    void fillMoviesEmptyFileTest() {

        FromFileFiller fff = createFiller("emptyFile.txt");

        Collection<Movie> movies = new ArrayList<>();

        fff.fillMovies(movies);

        assertTrue(movies.isEmpty());
    }

    @Test
    void fillMoviesFileNotExistTest() {

        String filepath = "NotExistFile.txt";
        var thrown = assertThrows(IllegalArgumentException.class, () -> new FromFileFiller(filepath));
        assertEquals("File does not exist: " + filepath, thrown.getMessage());
    }

    @Test
    void fillMoviesFileExtraFieldsTest() {

        FromFileFiller fff = createFiller("extraField.txt");

        Collection<Movie> movies = new ArrayList<>();

        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));

        String line = "The Shawshank Redemption;1994;2.4;1972";
        assertEquals("Invalid string format: " + line, thrown.getMessage());
    }

    @Test
    void fillMoviesFileMissingFieldsTest() {

        FromFileFiller fff = createFiller("missingField.txt");

        Collection<Movie> movies = new ArrayList<>();

        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));

        String line = "The Shawshank Redemption;1994";
        assertEquals("Invalid string format: " + line, thrown.getMessage());
    }

    @Test
    void fillMoviesFileInvalidSecondFieldTest() {

        FromFileFiller fff = createFiller("invalidSecondField.txt");

        Collection<Movie> movies = new ArrayList<>();

        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));

        String line = "qwerty";
        assertEquals("Invalid year: " + line, thrown.getMessage());
    }

    @Test
    void fillMoviesFileInvalidThirdFieldTest() {

        FromFileFiller fff = createFiller("invalidThirdField.txt");

        Collection<Movie> movies = new ArrayList<>();

        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));

        String line = "qwerty";
        assertEquals("Invalid hour: " + line, thrown.getMessage());
    }

    @Test
    void fillMoviesWithNullMoviesArgumentTest() {

        FromFileFiller fff = createFiller("correctMovies.txt");

        var thrown = assertThrows(NullPointerException.class, () -> fillMovies(fff, null));

        assertEquals("Collection<Movie> movies must be non null to fillMovies", thrown.getMessage());
    }
}
