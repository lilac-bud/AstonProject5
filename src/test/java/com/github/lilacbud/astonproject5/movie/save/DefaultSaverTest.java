package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.user.Menu;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
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
    private Menu<DefaultSaver> setSaveOptionMenu;

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
    
    private void configureSetSaveOptionMenuMock() {
        doAnswer(i -> {
            Scanner scanner = i.getArgument(0);
            DefaultSaver client = i.getArgument(1);
            while (true) {
                switch (scanner.nextLine()) {
                    case "1" -> { 
                        client.setSaveOption(StandardOpenOption.TRUNCATE_EXISTING);
                        return null;
                    }
                    case "2" -> { 
                        client.setSaveOption(StandardOpenOption.APPEND);
                        return null;
                    }
                    default -> {}
                }
            }
        }).when(setSaveOptionMenu).chooseOptionAndExecute(any(), any());
    }

    @Test
    public void testCreateSaverWithNullFilepath() {
        System.out.println("DefaultSaver with null filepath");
        var thrown = assertThrows(NullPointerException.class, () -> new DefaultSaver(null, null, new Scanner("")));
        assertEquals(DefaultSaver.FILEPATH_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void testCreateSaverWithNullScanner() {
        System.out.println("DefaultSaver with null scanner");
        var thrown = assertThrows(NullPointerException.class, () -> new DefaultSaver("", null, null));
        assertEquals(DefaultSaver.SCANNER_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void testSave() throws Exception {
        configureMovieMock1();
        configureMovieMock2();
        configureMovieMock3();
        System.out.println("save");
        
        Path file = tempDir.resolve("movies.txt");
        List<Movie> movies = List.of(movie1,movie2,movie3);

        new DefaultSaver(file.toString(), null, new Scanner("")).save(movies);

        assertTrue(Files.exists(file));
        assertEquals(List.of("Криминальное чтиво;1994;2.5", "Интерстеллар;2014;3.0","Начало;2010;2.5"),
                Files.readAllLines(file));
    }

    @Test
    public void testSaveWithTruncateOption() throws Exception {
        System.out.println("save with truncate option");
        configureMovieMock1();
        configureSetSaveOptionMenuMock();
        
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, "old text\n");

        new DefaultSaver(file.toString(), setSaveOptionMenu, new Scanner("1\n")).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(1, lines.size());
        assertEquals("Криминальное чтиво;1994;2.5",lines.get(0));
    }

    @Test
    public void testSaveWithAppendOption() throws Exception {
        System.out.println("save with append option");
        configureMovieMock1();
        configureSetSaveOptionMenuMock();
        
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file,"Дюна;2021;2.6\n");

        new DefaultSaver(file.toString(), setSaveOptionMenu, new Scanner("2\n")).save(List.of(movie1));

        List<String> lines = Files.readAllLines(file);
        assertEquals(2,lines.size());
        assertEquals("Дюна;2021;2.6", lines.get(0));
        assertEquals("Криминальное чтиво;1994;2.5", lines.get(1));
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void testSaveGivenInvalidFilepath() {
        System.out.println("save given invalid filepath");
        assertThrows(InvalidPathException.class, () -> new DefaultSaver("Name:InvalidFile.txt", null, new Scanner("")));
    }

    @Test
    public void testSaveGivenNullMovies() {
        System.out.println("save given null movies");
        Path file = tempDir.resolve("movies.txt");
        List<Movie> movies = null;

        DefaultSaver ds = new DefaultSaver(file.toString(), null, new Scanner(""));

        var thrown = assertThrows(NullPointerException.class, () -> ds.save(movies));
        assertEquals(DefaultSaver.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}