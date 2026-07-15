package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.nio.file.InvalidPathException;

class DefaultSaverTest {

    @TempDir
    Path tempDir;

    private static final Movie movie1 = mock(Movie.class);
    private static final Movie movie2 = mock(Movie.class);
    private static final Movie movie3 = mock(Movie.class);

    @BeforeAll
    public static void setUp(){
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        when(movie1.getYearOfRelease()).thenReturn(1994);
        when(movie1.getHourLength()).thenReturn(2.5f);

        when(movie2.getName()).thenReturn("Интерстеллар");
        when(movie2.getYearOfRelease()).thenReturn(2014);
        when(movie2.getHourLength()).thenReturn(3f);

        when(movie3.getName()).thenReturn("Начало");
        when(movie3.getYearOfRelease()).thenReturn(2010);
        when(movie3.getHourLength()).thenReturn(2.5f);
    }

    @Test
    public void saveCreate() throws Exception {
        Path file = tempDir.resolve("movies.txt");
        List<Movie> movies = List.of(movie1,movie2,movie3);

        new DefaultSaver(file.toString(), new Scanner("")).save(movies);

        assertTrue(Files.exists(file));
        assertEquals(List.of("Криминальное чтиво;1994;2.5", "Интерстеллар;2014;3.0","Начало;2010;2.5"),
                Files.readAllLines(file));
    }

    @Test
    public void saveOverwrite() throws Exception {
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, "old text\n");

        new DefaultSaver(file.toString(), new Scanner("1\n")).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(1, lines.size());
        assertEquals("Криминальное чтиво;1994;2.5",lines.get(0));
    }

    @Test
    public void saveAdd() throws Exception {
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file,"Дюна;2021;2.6\n");

        new DefaultSaver(file.toString(), new Scanner("2\n")).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(2,lines.size());
        assertEquals("Дюна;2021;2.6", lines.get(0));
        assertEquals("Криминальное чтиво;1994;2.5", lines.get(1));
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void testSavePath() {
        assertThrows(InvalidPathException.class, () -> new DefaultSaver("Name:\0InvalidFile.txt",
                new Scanner("")));
    }

    @Test
    public void saveMoviesIsNull() {

        Path file = tempDir.resolve("movies.txt");
        List<Movie> movies = null;

        DefaultSaver ds = new DefaultSaver(file.toString(), new Scanner(""));

        NullPointerException exception = assertThrows(NullPointerException.class, () -> ds.save(movies));

        assertEquals("Collection<Movie> movies must be non null to save", exception.getMessage());
    }


}