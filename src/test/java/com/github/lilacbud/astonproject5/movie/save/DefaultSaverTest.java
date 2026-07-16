package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.user.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.InvalidPathException;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultSaverTest {

    @TempDir
    Path tempDir;

    @Mock
    private Movie movie1, movie2, movie3;
    @Mock
    private Menu.MenuOption<MoviesSaver> overwriteOption, addOption;
    @Mock
    private Menu<MoviesSaver> setSaveOptionMenu;

    private void configureMovieMock1() {
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        when(movie1.getYearOfRelease()).thenReturn(1994);
        when(movie1.getHourLength()).thenReturn(2.5f);
    }
    private void configureMovieMock2() {
        when(movie2.getName()).thenReturn("Интерстеллар");
        when(movie2.getYearOfRelease()).thenReturn(2014);
        when(movie2.getHourLength()).thenReturn(3f);
    }
    private void configureMovieMock3() {
        when(movie3.getName()).thenReturn("Начало");
        when(movie3.getYearOfRelease()).thenReturn(2010);
        when(movie3.getHourLength()).thenReturn(2.5f);
    }
    private void configureOverwriteOption() {
        doAnswer(i -> {
            MoviesSaver client = i.getArgument(0);
            client.setSaveOption(StandardOpenOption.TRUNCATE_EXISTING);
            return null;
        }).when(overwriteOption).execute(any());
    }
    private void configureAddOption() {
        doAnswer(i -> {
            MoviesSaver client = i.getArgument(0);
            client.setSaveOption(StandardOpenOption.APPEND);
            return null;
        }).when(addOption).execute(any());
    }
    private void configureSetSaveOptionMenuMock() {
        when(setSaveOptionMenu.chooseOption(any())).thenAnswer(i -> {
            Scanner scanner = i.getArgument(0);
            while (true) {
                switch (scanner.nextLine()) {
                    case "1" -> { return overwriteOption; }
                    case "2" -> { return addOption; }
                    default -> {}
                }
            }
        });
    }

    @Test
    public void testCreateSaverWithNullFilepath() {
        System.out.println("testCreateSaver with null filepath");
        var thrown = assertThrows(NullPointerException.class, () -> new DefaultSaver(null, new Scanner(""), null));
        assertEquals("Filepath must not be null", thrown.getMessage());
    }
    
    @Test
    public void testCreateSaverWithNullScanner() {
        System.out.println("testCreateSaver with null scanner");
        var thrown = assertThrows(NullPointerException.class, () -> new DefaultSaver("", null, null));
        assertEquals("Scanner must not be null", thrown.getMessage());
    }
    
    @Test
    public void testSaveCreate() throws Exception {
        System.out.println("testSaveCreate");
        configureMovieMock1();
        configureMovieMock2();
        configureMovieMock3();
        
        Path file = tempDir.resolve("movies.txt");
        List<Movie> movies = List.of(movie1,movie2,movie3);

        new DefaultSaver(file.toString(), new Scanner(""), null).save(movies);

        assertTrue(Files.exists(file));
        assertEquals(List.of("Криминальное чтиво;1994;2.5", "Интерстеллар;2014;3.0","Начало;2010;2.5"),
                Files.readAllLines(file));
    }

    @Test
    public void testSaveOverwrite() throws Exception {
        System.out.println("testSaveOverwrite");
        configureMovieMock1();
        configureOverwriteOption();
        configureSetSaveOptionMenuMock();
        
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, "old text\n");

        new DefaultSaver(file.toString(), new Scanner("1\n"), setSaveOptionMenu).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(1, lines.size());
        assertEquals("Криминальное чтиво;1994;2.5",lines.get(0));
    }

    @Test
    public void testSaveAdd() throws Exception {
        System.out.println("testSaveAdd");
        configureMovieMock1();
        configureAddOption();
        configureSetSaveOptionMenuMock();
        
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file,"Дюна;2021;2.6\n");

        new DefaultSaver(file.toString(), new Scanner("2\n"), setSaveOptionMenu).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(2,lines.size());
        assertEquals("Дюна;2021;2.6", lines.get(0));
        assertEquals("Криминальное чтиво;1994;2.5", lines.get(1));
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void testSaveGivenInvalidFilepath() {
        System.out.println("testSave given invalid filepath");
        assertThrows(InvalidPathException.class, () -> new DefaultSaver("Name:InvalidFile.txt", new Scanner(""), null));
    }

    @Test
    public void testSaveGivenNullMovies() {
        System.out.println("testSave given null movies");
        Path file = tempDir.resolve("movies.txt");
        List<Movie> movies = null;

        DefaultSaver ds = new DefaultSaver(file.toString(), new Scanner(""), null);

        var thrown = assertThrows(NullPointerException.class, () -> ds.save(movies));
        assertEquals("Collection<Movie> movies must be non null to save", thrown.getMessage());
    }
}