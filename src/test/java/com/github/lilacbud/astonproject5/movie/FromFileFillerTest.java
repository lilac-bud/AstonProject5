package com.github.lilacbud.astonproject5.movie;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.*;

public class FromFileFillerTest {

    private FromFileFiller createFiller(String filepath) {

        URL resource = getClass().getClassLoader().getResource(filepath);

        Objects.requireNonNull(resource, "resource must be non null to save");

        try {
            return new FromFileFiller(Paths.get(resource.toURI()).toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot load test resource: " + filepath, e);
        }
    }

    private void fillMovies(FromFileFiller fff, Collection<Movie> movies) {
        try (MockedStatic<MovieInputValidation> validation =
                     mockStatic(MovieInputValidation.class)) {
            validation.when(() -> MovieInputValidation
                            .validateName(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> MovieInputValidation
                            .validateYearOfRelease(anyString()))
                    .thenAnswer(i ->
                            Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation
                            .validateYearOfRelease("qwerty"))
                    .thenReturn(Optional.empty());
            validation.when(() -> MovieInputValidation
                            .validateHourLength(anyString()))
                    .thenAnswer(i ->
                            Optional.of(Float.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation
                            .validateHourLength("qwerty"))
                    .thenReturn(Optional.empty());

            fff.fillMovies(movies);
        }
    }

    @Test
    void fromFileFillerFilepathIsNullTest() {

        NullPointerException exception = assertThrows(NullPointerException.class, () -> new FromFileFiller(null));
        assertEquals("Filepath must not be null", exception.getMessage());
    }

    @Test
    void fillMoviesCorrectTest() {

        FromFileFiller fff = createFiller("correctMovies.txt");

        List<Movie> movies = new ArrayList<>();

        fillMovies(fff, movies);

        assertEquals(5, movies.size());
        assertEquals("The Shawshank Redemption", movies.get(0).getName());
        assertEquals(1994, movies.get(0).getYearOfRelease());
        assertEquals(2.4f, movies.get(0).getHourLength());

        assertEquals("The Godfather", movies.get(1).getName());
        assertEquals(1972, movies.get(1).getYearOfRelease());
        assertEquals(2.9f, movies.get(1).getHourLength());

        assertEquals("The Matrix", movies.get(2).getName());
        assertEquals(1999, movies.get(2).getYearOfRelease());
        assertEquals(2.3f, movies.get(2).getHourLength());

        assertEquals("Inception", movies.get(3).getName());
        assertEquals(2010, movies.get(3).getYearOfRelease());
        assertEquals(2.5f, movies.get(3).getHourLength());

        assertEquals("Interstellar", movies.get(4).getName());
        assertEquals(2014, movies.get(4).getYearOfRelease());
        assertEquals(2.8f, movies.get(4).getHourLength());

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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FromFileFiller(filepath));
        assertEquals("File does not exist: " + filepath, exception.getMessage());
    }

    @Test
    void fillMoviesFileExtraFieldsTest() {

        FromFileFiller fff = createFiller("extraField.txt");

        Collection<Movie> movies = new ArrayList<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fff.fillMovies(movies);
        });

        String line = "The Shawshank Redemption;1994;2.4;1972";
        assertEquals("Invalid string format: " + line, exception.getMessage());
    }

    @Test
    void fillMoviesFileMissingFieldsTest() {

        FromFileFiller fff = createFiller("missingField.txt");

        Collection<Movie> movies = new ArrayList<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fff.fillMovies(movies));

        String line = "The Shawshank Redemption;1994";
        assertEquals("Invalid string format: " + line, exception.getMessage());
    }

    @Test
    void fillMoviesFileInvalidSecondFieldTest() {

        FromFileFiller fff = createFiller("invalidSecondField.txt");

        Collection<Movie> movies = new ArrayList<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fff.fillMovies(movies));

        String line = "qwerty";
        assertEquals("Invalid year: " + line, exception.getMessage());
    }

    @Test
    void fillMoviesFileInvalidThirdFieldTest() {

        FromFileFiller fff = createFiller("invalidThirdField.txt");

        Collection<Movie> movies = new ArrayList<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fff.fillMovies(movies));

        String line = "qwerty";
        assertEquals("Invalid hour: " + line, exception.getMessage());
    }

    @Test
    void fillMoviesWithNullMoviesArgumentTest() {

        FromFileFiller fff = createFiller("correctMovies.txt");

        NullPointerException exception = assertThrows(NullPointerException.class, () -> fff.fillMovies(null));

        assertEquals("Collection<Movie> movies must be non null to fillMovies", exception.getMessage());
    }
}
