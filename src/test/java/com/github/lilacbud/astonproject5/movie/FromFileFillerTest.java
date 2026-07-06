package com.github.lilacbud.astonproject5.movie;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FromFileFillerTest {

    private FromFileFiller createFiller(String filepath) {
        try {
            String path = Paths.get(
                    getClass().getClassLoader()
                            .getResource(filepath)
                            .toURI()
            ).toString();
            return new FromFileFiller(path);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load test resource: " + filepath, e);
        }
    }

    @Test
    void fillMoviesCorrectTest() {

        FromFileFiller fff = createFiller("correctMovies.txt");

        List<Movie> movies = new ArrayList<>(); //OIGUL тут будет Collection, если реализуем

        fff.fillMovies(movies);

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

        List<Movie> movies = new ArrayList<>(); //OIGUL тут будет Collection, если реализуем

        fff.fillMovies(movies);

        assertTrue(movies.isEmpty());
    }

    @Test
    void fillMoviesFileNotExistTest() {

        assertThrows(IllegalArgumentException.class, () -> {
            new FromFileFiller("NotExistFile.txt");
        });
    }

    @Test
    void fillMoviesFileExtraFieldsTest() {

        FromFileFiller fff = createFiller("extraField.txt");

        List<Movie> movies = new ArrayList<>(); //OIGUL тут будет Collection, если реализуем

        assertThrows(IllegalArgumentException.class, () -> {
            fff.fillMovies(movies);
        });
    }

    @Test
    void fillMoviesFileMissingFieldsTest() {

        FromFileFiller fff = createFiller("missingField.txt");

        List<Movie> movies = new ArrayList<>(); //OIGUL тут будет Collection, если реализуем

        assertThrows(IllegalArgumentException.class, () -> {
            fff.fillMovies(movies);
        });
    }

    @Test
    void fillMoviesFileInvalidSecondFieldTest() {

        FromFileFiller fff = createFiller("invalidSecondField.txt");

        List<Movie> movies = new ArrayList<>(); //OIGUL тут будет Collection, если реализуем

        assertThrows(IllegalArgumentException.class, () -> {
            fff.fillMovies(movies);
        });
    }

    @Test
    void fillMoviesFileInvalidThirdFieldTest() {

        FromFileFiller fff = createFiller("invalidThirdField.txt");

        List<Movie> movies = new ArrayList<>(); //OIGUL тут будет Collection, если реализуем

        assertThrows(IllegalArgumentException.class, () -> {
            fff.fillMovies(movies);
        });
    }
}
