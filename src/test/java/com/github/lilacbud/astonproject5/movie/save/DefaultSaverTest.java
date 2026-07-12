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

class DefaultSaverTest {

    @TempDir
    Path tempDir;

    private static final Movie movie1 = mock(Movie.class);
    private static final Movie movie2 = mock(Movie.class);
    private static final Movie movie3 = mock(Movie.class);

    @BeforeAll
    static void setUp(){
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
    public void saveOverwrite() throws Exception {
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, "old text\n");

        new DefaultSaver(file.toString(), new Scanner("1\n")).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(1, lines.size());
        assertEquals("Криминальное чтиво;1994;2.5",lines.get(0));
    }

    @Test
    void testSave() {
    }
}