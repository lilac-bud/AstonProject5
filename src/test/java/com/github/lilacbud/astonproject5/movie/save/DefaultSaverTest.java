package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.user.Menu;
import java.io.IOException;
import java.nio.file.Files;
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
    private final String filename = "movies.txt";
    
    @TempDir
    private Path tempDir;

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
    public void givenNullAsFilepath_whenCreatingDefaultSaver_thenThrow() {
        final String filepath = null;
        final var thrown = assertThrows(NullPointerException.class, () -> new DefaultSaver(filepath));
        assertEquals(DefaultSaver.FILEPATH_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenMenuAndNullAsScanner_whenCreatingDefaultSaver_thenThrow() {
        final String filepath = "";
        final Scanner scanner = null;
        final var thrown = assertThrows(NullPointerException.class, 
                () -> new DefaultSaver(filepath, setSaveOptionMenu, scanner));
        assertEquals(DefaultSaver.SCANNER_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenThatFileExistsNot_whenSavingCollection_thenCreateFile() throws IOException {
        configureMovieMock1();
        configureMovieMock2();
        configureMovieMock3();
        final Path path = tempDir.resolve(filename);
        final DefaultSaver ds = new DefaultSaver(path.toString());
        final List<Movie> movies = List.of(movie1,movie2,movie3);
        final List<String> expectedLines = List.of(
                "Криминальное чтиво;1994;2.5", 
                "Интерстеллар;2014;3.0",
                "Начало;2010;2.5");
        ds.save(movies);
        assertTrue(Files.exists(path));
        assertEquals(expectedLines, Files.readAllLines(path));
    }

    @Test
    public void givenThatFileExists_whenSavingCollectionAndChoosingTruncate_thenOverwriteFile() throws Exception {
        configureMovieMock1();
        configureSetSaveOptionMenuMock();
        final Path path = tempDir.resolve(filename);
        Files.writeString(path, "old text\n");
        final String input = "1\n";
        final DefaultSaver ds = new DefaultSaver(path.toString(), setSaveOptionMenu, new Scanner(input));
        final List<String> expectedLines = List.of("Криминальное чтиво;1994;2.5");
        ds.save(List.of(movie1));
        List<String> lines = Files.readAllLines(path);
        assertEquals(expectedLines, lines);
    }

    @Test
    public void givenThatFileExists_whenSavingCollectionAndChoosingAppend_thenAppendLinesToFile() throws Exception {
        configureMovieMock1();
        configureSetSaveOptionMenuMock();
        final Path path = tempDir.resolve(filename);
        final String existingLine = "Дюна;2021;2.6\n";
        Files.writeString(path, existingLine);
        final String input = "2\n";
        final DefaultSaver ds = new DefaultSaver(path.toString(), setSaveOptionMenu, new Scanner(input));
        final List<String> expectedLines = List.of(existingLine.trim(), "Криминальное чтиво;1994;2.5");
        ds.save(List.of(movie1));
        final List<String> lines = Files.readAllLines(path);
        assertEquals(expectedLines, lines);
    }

    @Test
    public void givenNullAsCollection_whenSavingCollection_thenThrow() {
        final Path path = tempDir.resolve(filename);
        final List<Movie> movies = null;
        DefaultSaver ds = new DefaultSaver(path.toString());
        final var thrown = assertThrows(NullPointerException.class, () -> ds.save(movies));
        assertEquals(DefaultSaver.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}